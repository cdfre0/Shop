package cdpr.web.repository;

import cdpr.web.resources.Game;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author cdfre
 */
public interface GameRepository extends JpaRepository<Game, Integer>{
}
