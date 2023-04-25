/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cdpr.web.service.impl;

import cdpr.web.repository.GameRepository;
import cdpr.web.repository.UserRepository;
import cdpr.web.resources.User;
import cdpr.web.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
        User user = new User("admin", "admin1", true);
        repository.save(user);
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
    public String addUser(String login, String password) {
        if (repository.existsById(login)) {
            return "Such login already exist";
        }
        repository.save(new User(login, password));
        return "User created";
    }

    @Override
    public String promoteUser(String login) {
        Optional<User> optUser = repository.findById(login);
        if(optUser.isPresent()) {
            optUser.get().setPermission(true);
            repository.save(optUser.get());
            return "Promotion successed";
        }
        return "Such user does not exist";
    }

    @Override
    public String deleteUser(String login) {
        if(repository.existsById(login)) {
            repository.deleteById(login);
            return "Deletion Successed";
        }
        return "Such user does not exist";
    }

    @Override
    public String updatePassword(String login, String oldPassword, String newPassword) {
        Optional<User> optUser = repository.findById(login);
        if(optUser.isPresent()) {
            if(optUser.get().getPassword().equals(oldPassword)){
                optUser.get().setPassword(newPassword);
                repository.save(optUser.get());
                return "Password updated";
            }
            return "Old password is incorrect";
        }
        return "Such user does not exist";
    }

}
