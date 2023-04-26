package cdpr.web.service.impl;

import cdpr.web.exception.ObjectNotFoundException;
import cdpr.web.repository.UserRepository;
import cdpr.web.resources.User;
import cdpr.web.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.springframework.stereotype.Service;

/**
 * Class implements UserService interface. Use to communicate with Repository of
 * Users and change it's instances. Supports CRUD methods.
 *
 * @author Jan Michalec
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * Repository for saving data.
     */
    UserRepository repository;

    /**
     * Populating repository on start.
     *
     * @param repository UserRepository, repository this Service will be using
     */
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
        User user = new User("admin", "admin1");
        user.setPermission(true);
        repository.save(user);
        user.setPermission(false);
        user = new User("login", "password");
        repository.save(user);

    }

    //CREATE
    /**
     * Method saves User in repository if it's a new one.
     *
     * @param newUser User to save
     * @return String information of success or error
     */
    @Override
    public String addUser(User newUser) {
        if (repository.existsById(newUser.getLogin())) {
            return "Such login already exist";
        }
        newUser.setPermission(false);
        repository.save(newUser);
        return "User created";
    }

    //GET
    /**
     * Method retrieves details of all users from repository.
     *
     * @return List of all Users logins and permissions
     */
    @Override
    public List<String> getUsers() {
        List<String> loginWithPermission = new ArrayList<>();
        for (User user : repository.findAll()) {
            loginWithPermission.add(user.getLoginAndPerrmision());
        }
        return loginWithPermission;
    }

    //UPDATE
    /**
     * Method promotes User to an admin, if it is not already and exist.
     *
     * @param login User login
     * @return String of confirmation or error
     */
    @Override
    public String promoteUser(String login) {
        Optional<User> optUser = repository.findById(login);
        if (optUser.isPresent()) {
            if (optUser.get().getPermission()) {
                return "User is already an admin";
            }
            optUser.get().setPermission(true);
            repository.save(optUser.get());
            return "Promotion successed";
        }
        return "Such user does not exist";
    }

    //DELETE
    /**
     * Method deletes User from repository, if it exist.
     *
     * @param login User login
     * @return String confirmation of success or error
     */
    @Override
    public String deleteUser(String login) {
        checkLoginExisting(login);
        repository.deleteById(login);
        return "Deletion Successed";
    }

    /**
     * Checks if user exist in repository.
     *
     * @param newUser User to check
     * @return User instance if exist, null otherwise;
     */
    @Override
    public User verifyUser(User newUser) {
        checkLoginExisting(newUser.getLogin());
        User user = repository.findById(newUser.getLogin()).get();
        if (user.getPassword().
                equals(newUser.getPassword())) {
            return user;
        }
        return null;
    }

    /**
     * Method checks if User of given login exist in repository. 
     * //throws Error if does not exist
     *
     * @param login User login
     */
    private void checkLoginExisting(String login) {
        if (!repository.existsById(login)) {
            throw new ObjectNotFoundException(
                    "User with such ID does not exist in Repository");
        }
    }

}
