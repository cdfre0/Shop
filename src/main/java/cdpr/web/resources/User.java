package cdpr.web.resources;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

/**
 *
 * @author Jan Michalec
 */
@Entity
@Table(name = "user_database")
public class User {

    @Id
    private String login;
    private String password;
    private Boolean permission;
    
    public User() {  
    }
    public User(String login) {
        this.login = login;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getPermission() {
        return permission;
    }

    public void setPermission(Boolean permission) {
        this.permission = permission;
    }

    public String getLoginAndPerrmision() {
        return login + ", " + permission;
    }
}
