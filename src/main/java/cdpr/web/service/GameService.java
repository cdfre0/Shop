package cdpr.web.service;

import cdpr.web.resources.Game;
import java.util.List;

/**
 * Interface of Game Service with all methods such interface should implement.
 *
 * @author Jan Michalec
 */
public interface GameService {

    
    
    //Create
    public String createGame(Game game);

    //Gets
    public Game getGame(Integer id);

    public boolean isAvailable(Integer id);

    public List<Game> getAllGames();

    public List<Game> findGameByName(String name);

    public List<Game> getGameByDev(String developerName);

    public List<Game> getGameByGenre(Game.Genre genre);

    public List<Game> showStockLessThan(double price);

    //Update
    public String buyOneGame(Integer id);

    public String restockGame(Integer id, int quantity);
    
    public String addGenreToGame(Integer id, Game.Genre genre);
    
    public String deleteGenreFromGame(Integer id, Game.Genre genre);

    public String putGameOnSale(Integer id, double factor);

    //Delete
    public String deleteGame(Integer id);

    public String deleteAllUnstockGames();

}
