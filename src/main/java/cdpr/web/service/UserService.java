package cdpr.web.service;

import cdpr.web.resources.User;
import java.util.List;

/**
 *
 * @author Jan Michalec
 */
public interface UserService {

    public List<String> getUsers();

    public String addUser(User newUser);

    public String promoteUser(String login);

    public String deleteUser(String login);
    
 //   public String updatePassword(String login, String oldPassword, String newPassword);
    public User verifyUser(User newUser);
}
