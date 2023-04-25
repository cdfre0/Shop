/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cdpr.web.service.impl;

import cdpr.web.exception.GameNotFoundException;
import cdpr.web.repository.UserRepository;
import cdpr.web.resources.User;
import cdpr.web.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jan Michalec
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * Repository for saving data.
     */
    UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
        User user = new User("admin", "admin1");
        user.setPermission(true);
        repository.save(user);
        user.setPermission(false);
        user = new User("login", "password");
        repository.save(user);
    }

    @Override
    public List<String> getUsers() {
        List<String> loginWithPermission = new ArrayList<>();
        for (User user : repository.findAll()) {
            loginWithPermission.add(user.getLoginAndPerrmision());
        }
        return loginWithPermission;
    }

    @Override
    public String addUser(User newUser) {
        if (repository.existsById(newUser.getLogin())) {
            return "Such login already exist";
        }
        newUser.setPermission(false);
        repository.save(newUser);
        return "User created";
    }

    @Override
    public String promoteUser(String login) {
        Optional<User> optUser = repository.findById(login);
        if (optUser.isPresent()) {
            if(optUser.get().getPermission()){
                return "User is already an admin";
            }
            optUser.get().setPermission(true);
            repository.save(optUser.get());
            return "Promotion successed";
        }
        return "Such user does not exist";
    }

    @Override
    public String deleteUser(String login) {
        checkLoginExisting(login);
        repository.deleteById(login);
        return "Deletion Successed";

    }

//    @Override
//    public String updatePassword(String login, String oldPassword, String newPassword) {
//        checkLoginExisting(login);
//        User user = repository.findById(login).get();
//
//        if (user.getPassword().equals(oldPassword)) {
//            user.setPassword(newPassword);
//            repository.save(user);
//            return "Password updated";
//        }
//        return "Old password is incorrect";
//    }

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

    private void checkLoginExisting(String login) {
        if (!repository.existsById(login)) {
            throw new GameNotFoundException(
                    "Game with such ID does not exist in Repository");
        }
    }

}
