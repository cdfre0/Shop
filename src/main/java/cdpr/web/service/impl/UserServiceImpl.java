package cdpr.web.service.impl;

import cdpr.web.exception.GameNotFoundException;
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
     * Lock for reading and writing data in database.
     */
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

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
        lock.writeLock().lock();
        try {
            if (repository.existsById(newUser.getLogin())) {
                return "Such login already exist";
            }
            newUser.setPermission(false);
            repository.save(newUser);
            return "User created";
        } finally {
            lock.writeLock().unlock();
        }
    }

    //GET
    /**
     * Method retrieves details of all users from repository.
     *
     * @return List of all Users logins and permissions
     */
    @Override
    public List<String> getUsers() {
        lock.readLock().lock();
        try {
            List<String> loginWithPermission = new ArrayList<>();
            for (User user : repository.findAll()) {
                loginWithPermission.add(user.getLoginAndPerrmision());
            }
            return loginWithPermission;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Checks if user exist in repository.
     *
     * @param newUser User to check
     * @return User instance if exist, null otherwise;
     */
    @Override
    public User verifyUser(User newUser) {
        lock.readLock().lock();
        try {
            checkLoginExisting(newUser.getLogin());
            User user = repository.findById(newUser.getLogin()).get();
            if (user.getPassword().
                    equals(newUser.getPassword())) {
                return user;
            }
            return null;
        } finally {
            lock.readLock().unlock();
        }
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
        lock.writeLock().lock();
        try {
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
        } finally {
            lock.writeLock().unlock();
        }

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
        lock.writeLock().lock();
        try {
            checkLoginExisting(login);
            repository.deleteById(login);
            return "Deletion Successed";
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Method checks if User of given login exist in repository.
     *
     * @param login User login
     * @throws Error if does not exist
     */
    private void checkLoginExisting(String login) {
        if (!repository.existsById(login)) {
            //TODO change
            throw new GameNotFoundException(
                    "Game with such ID does not exist in Repository");
        }
    }

}
