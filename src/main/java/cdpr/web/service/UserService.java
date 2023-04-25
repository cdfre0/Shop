package cdpr.web.service;

import cdpr.web.resources.User;
import java.util.List;

/**
 * Interface of User Service.
 *
 * @author Jan Michalec
 */
public interface UserService {

    public List<String> getUsers();

    public String addUser(User newUser);

    public String promoteUser(String login);

    public String deleteUser(String login);

    public User verifyUser(User newUser);
}
