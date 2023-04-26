//package cdpr.web.service.impl;
//
//import cdpr.web.repository.GameRepository;
//import cdpr.web.resources.Game;
//import cdpr.web.service.GameService;
//import java.util.Arrays;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//
///**
// *
// * @author Jan Michalec
// */
//public class GameServiceImplTest {
//
//    @Autowired
//    private GameRepository repository;
//    private GameService service;
//    AutoCloseable autoCloseable;
//    Game game;
//
//    @BeforeEach
//    public void setUp() {
//        autoCloseable = MockitoAnnotations.openMocks(this);
//        service = new GameServiceImpl(repository);
//        game = new Game("Heroes", "Nival", Arrays.asList(Game.Genre.STRATEGY), 251.0, 2);
//
//    }
//
//    @AfterEach
//    public void tearDown() throws Exception {
//        autoCloseable.close();
//    }
//
//    /**
//     * Test of createGame method, of class GameServiceImpl.
//     */
//    @Test
//    public void testCreateGame_Success() {
//        mock(Game.class);
//        mock(GameRepository.class);
//        when(repository.save(game)).thenReturn(game);
//        assertThat(service.createGame(game)).isEqualTo("Create OK");
//        System.out.println(service.getAllGames());
//    }
//
//    @Test
//    public void testCreateGame_Game_Already_Exist() {
////        mock(Game.class);
////        mock(GameRepository.class);
//
//        service.createGame(game);
//        System.out.println(service.getAllGames());
//        System.out.println(repository.count());
//        game.setId(0);
//        
//        assertThat(service.createGame(game)).isEqualTo("Game already exist in Repository");
//    }
//
//    @Test
//    public void testCreateGame_Game_Allocate_Deleted_Number() {
//        mock(Game.class);
//        mock(GameRepository.class);
//
//        service.createGame(game);
//        game = new Game("Gloomhaven", "Isaac", Arrays.asList(Game.Genre.STRATEGY), 21.0, 7);
//        service.createGame(game);
//        game = new Game("Mario", "Nintendo", Arrays.asList(Game.Genre.STRATEGY), 121.0, 100);
//        service.createGame(game);
//        service.deleteGame(1);
//        game = new Game("Mario II", "Nintendo", Arrays.asList(Game.Genre.STRATEGY), 125.0, 1200);
//
//        assertThat(service.getGame(1).getName()).isEqualTo(game.getName());
//    }
//
//    /**
//     * Test of getGame method, of class GameServiceImpl.
//     */
//    @Test
//    public void testGetGame() {
//
//    }
//
//    /**
//     * Test of findGameByName method, of class GameServiceImpl.
//     */
//    @Test
//    public void testFindGameByName() {
//
//    }
//
//    /**
//     * Test of getAllGames method, of class GameServiceImpl.
//     */
//    @Test
//    public void testGetAllGames() {
//
//    }
//
//    /**
//     * Test of getGameByDev method, of class GameServiceImpl.
//     */
//    @Test
//    public void testGetGameByDev() {
//
//    }
//
//    /**
//     * Test of getGameByGenre method, of class GameServiceImpl.
//     */
//    @Test
//    public void testGetGameByGenre() {
//
//    }
//
//    /**
//     * Test of showStockLessThan method, of class GameServiceImpl.
//     */
//    @Test
//    public void testShowStockLessThan() {
//
//    }
//
//    /**
//     * Test of isAvailable method, of class GameServiceImpl.
//     */
//    @Test
//    public void testIsAvailable() {
//
//    }
//
//    /**
//     * Test of buyOneGame method, of class GameServiceImpl.
//     */
//    @Test
//    public void testBuyOneGame() {
//
//    }
//
//    /**
//     * Test of restockGame method, of class GameServiceImpl.
//     */
//    @Test
//    public void testRestockGame() {
//
//    }
//
//    /**
//     * Test of putGameOnSale method, of class GameServiceImpl.
//     */
//    @Test
//    public void testPutGameOnSale() {
//
//    }
//
//    /**
//     * Test of deleteGame method, of class GameServiceImpl.
//     */
//    @Test
//    public void testDeleteGame() {
//
//    }
//
//    /**
//     * Test of deleteAllUnstockGames method, of class GameServiceImpl.
//     */
//    @Test
//    public void testDeleteAllUnstockGames() {
//    }
//
//}
