package cdpr.web.repository;

import cdpr.web.resources.Game;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface of JpaRepository using Game and Integer to make repository. Id of
 * Game is used as primary key.
 *
 * @author Jan Michalec
 */
public interface GameRepository extends JpaRepository<Game, Integer> {

    List<Game> findAllByName(String name);
}
