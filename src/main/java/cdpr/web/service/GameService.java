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

    public String isAvailable(Integer id);

    public List<Game> getAllGames();

    public List<Game> getGameByDev(String developerName);

    public List<Game> getGameByGenre(Game.Genre genre);

    public List<Game> showStockLessThan(double price);

    //Update
    public String buyOneGame(Integer id);

    public String restockGame(Integer id, int quantity);

    public String putGameOnSale(Integer id, double factor);

    //Delete
    public String deleteGame(Integer id);

    public String deleteAllUnstockGames();

}
