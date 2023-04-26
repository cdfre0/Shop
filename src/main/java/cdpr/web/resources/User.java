package cdpr.web.resources;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Class stores data for user Object.
 *
 * @author Jan Michalec
 */
@Entity
@Table(name = "user_database")
public class User {

    /**
     * String login of User. Each User has unique one.
     */
    @Id
    private String login;
    /**
     * String password of user to log in.
     */
    private String password;
    /**
     * Boolean indicating permissions of user. 0 for user, 1 for admin.
     */
    private Boolean permission;
    /**
     * Plain constructor
     */
    public User() {
    }
    /**
     * Constructor with only login for checking.
     * @param login String login
     */
    public User(String login) {
        this.login = login;
    }
    /**
     * Constructor of login class on creation.
     * @param login String login
     * @param password String password 
     */
    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     * Gets login of User.
     *
     * @return String login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Gets password of User.
     *
     * @return String password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password of user to new one.
     *
     * @param password String new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets permission of User.
     *
     * @return Boolean permission
     */
    public Boolean getPermission() {
        return permission;
    }

    /**
     * Sets permission of User to new one.
     *
     * @param permission Boolean new permission.
     */
    public void setPermission(Boolean permission) {
        this.permission = permission;
    }

    /**
     * ToString showing login and permissions of User.
     *
     * @return String login with permission
     */
    public String getLoginAndPerrmision() {
        return login + ", " + permission;
    }
}
