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
    private final String login;
    private String password;
    private Boolean permission;

    public User(String login) {
        this.login = login;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        permission = false;
    }

    public User(String login, String password, Boolean permission) {
        this.login = login;
        this.password = password;
        this.permission = permission;
    }

    public String getLogin() {
        return password;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        return Objects.equals(this.login, other.login);
    }
}
