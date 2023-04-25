package cdpr.web.repository;

import cdpr.web.resources.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface of JpaRepository using User and String to make repository. Login of
 * User is used as primary key.
 *
 * @author Jan Michalec
 */
public interface UserRepository extends JpaRepository<User, String> {
}
