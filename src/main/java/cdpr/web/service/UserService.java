package cdpr.web.service;

import cdpr.web.resources.User;
import java.util.List;

/**
 * Interface of User Service.
 *
 * @author Jan Michalec
 */
public interface UserService {

    /**
     * Method saves User in repository if it's a new one.
     *
     * @param newUser User to save
     * @return String information of success or error
     */
    public String addUser(User newUser);

    /**
     * Method retrieves details of all users from repository.
     *
     * @return List of all Users logins and permissions
     */
    public List<String> getUsers();

    /**
     * Method promotes User to an admin, if it is not already and exist.
     *
     * @param login User login
     * @return String of confirmation or error
     */
    public String promoteUser(String login);

    /**
     * Method deletes User from repository, if it exist.
     *
     * @param login User login
     * @return String confirmation of success or error
     */
    public String deleteUser(String login);

    /**
     * Checks if user exist in repository.
     *
     * @param newUser User to check
     * @return User instance if exist, null otherwise;
     */
    public User verifyUser(User newUser);
}
