package cdpr.web.repository;

import cdpr.web.resources.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Jan Michalec
 */
public interface UserRepository extends JpaRepository<User, String> {
}
