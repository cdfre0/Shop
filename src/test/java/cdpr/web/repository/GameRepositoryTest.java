package cdpr.web.repository;

import cdpr.web.resources.Game;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Test class to test custom GameRepository methods.
 * @author Jan Michaelc
 */
@DataJpaTest
public class GameRepositoryTest {

    @Autowired
    private GameRepository repository;
    Game game;
    Game game2;
    Game game3;

    /**
     * Method called each time test case is called.
     */
    @BeforeEach
    void setUp() {
        game = new Game("Gloomhaven", "Isaac", Arrays.asList(Game.Genre.STRATEGY), 25.0, 5);
        game.setId(0);
        repository.save(game);
        game2 = new Game("Gloomhaven", "not Isaac", Arrays.asList(Game.Genre.RPG), 125.0, 0);
        game2.setId(1);
        repository.save(game2);
        game3 = new Game("Heroes", "Nival", Arrays.asList(Game.Genre.STRATEGY), 225.0, 25);
        game3.setId(2);
        repository.save(game3);
    }
    
    /**
     * Method called each time test case is finished.
     */
    @AfterEach()
    void tearDown() {
        game = null;
        game2 = null;
        game3 = null;
        repository.deleteAll();
    }
    //SUCCESS
    @Test
    void testFindByName_Found_One() {
        List<Game> gameList = repository.findAllByName("Heroes");
        assertThat(gameList.get(0).getName()).isEqualTo(game3.getName());
        
    }
    @Test
    void testFindByName_Found_More() {
        List<Game> gameList = repository.findAllByName("Gloomhaven");
        assertThat(gameList.get(0).getName()).isEqualTo(game.getName());
        assertThat(gameList.get(1).getName()).isEqualTo(game2.getName());
        
    }
    
    //FAILURES
    @Test
    void testFindByName_Found_None() {
        List<Game> gameList = repository.findAllByName("Cyberpunk");
        assertThat(gameList.isEmpty()).isTrue();
    }
    
}
