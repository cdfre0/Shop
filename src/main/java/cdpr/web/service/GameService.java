package cdpr.web.service;

import cdpr.web.resources.Game;
import java.util.List;

/**
 * Interface of Game Service.
 *
 * @author Jan Michalec
 */
public interface GameService {

    //Create
    /**
     * Method saves game in repository if it does not exist there.
     *
     * @param game Game to save
     * @return String information of success or error
     */
    public String createGame(Game game);

    /**
     * Method changes game stored at specific id to new one.
     *
     * @param id Integer id of game to change
     * @param game Game new game
     * @return String confirmation of success or error
     */
    public String rewriteGame(Integer id, Game game);

    //Gets
    /**
     * Method retrieves details of Game from repository given id.
     *
     * @param id Integer id of Game
     * @return Game instance or error message
     */
    public Game getGame(Integer id);

    /**
     * Method retrieves details of all Games that has given name.
     *
     * @param name String name to check
     * @return List of Games
     */
    public List<Game> findGameByName(String name);

    /**
     * Method retrieves details of all games from repository.
     *
     * @return List of Games
     */
    public List<Game> getAllGames();

    /**
     * Method retrieves details of games containing given developer name.
     *
     * @param developerName String developerName to check in Game instance
     * @return List of Games
     */
    public List<Game> getGameByDev(String developerName);

    /**
     * Method retrieves details of games containing given Genre type.
     *
     * @param genre Genre type to check in Game instance
     * @return List of Games
     */
    public List<Game> getGameByGenre(Game.Genre genre);

    /**
     * Method retrieves details of games that price is less than given.
     *
     * @param price double an upper boundary to check Games' price
     * @return List of Games
     */
    public List<Game> showStockLessThan(double price);

    /**
     * Method checks if Game has any copies in repository, given id.
     *
     * @param id Integer id of Game
     * @return boolean true if exist
     */
    public boolean isAvailable(Integer id);

    //Update
    /**
     * Method decreases Quantity of Games Quantity, given it's id.
     *
     * @param id Integer id of Game
     * @return String confirmation of success or error
     */
    public String buyOneGame(Integer id);

    /**
     * Method adds quantity of Game given number to add and game's id.
     *
     * @param id Integer id of Game
     * @param quantity int number to add
     * @return String confirmation of success or error
     */
    public String restockGame(Integer id, int quantity);

    /**
     * Method changes name of Game to new one.
     *
     * @param id Integer id of game name to change
     * @param name String new name
     * @return String confirmation of success or error
     */
    public String changeName(Integer id, String name);

    /**
     * Method changes developer name of Game to new one.
     *
     * @param id Integer id of game developer name to change
     * @param developer String new developer name
     * @return String confirmation of success or error
     */
    public String changeDeveloper(Integer id, String developer);

    /**
     * Method changes price of Game given factor to multiply price and game's
     * id.
     *
     * @param id Integer id of Game
     * @param factor double factor that multiplies price
     * @return String confirmation of success or error
     */
    public String putGameOnSale(Integer id, double factor);

    /**
     * Method adds genre to Game's list of genre, given game's id and genre.
     *
     * @param id Integer id of Game
     * @param genre Genre type
     * @return String confirmation of success or error
     */
    public String addGenreToGame(Integer id, Game.Genre genre);

    /**
     * Method deletes genre from Game's list of genre, given game's id and
     * genre.
     *
     * @param id Integer id of Game
     * @param genre Genre type
     * @return String confirmation of success or error
     */
    public String deleteGenreFromGame(Integer id, Game.Genre genre);

    //Delete
    /**
     * Method deletes Game from repository given Game's id.
     *
     * @param id Integer id of Game
     * @return String confirmation of success or error
     */
    public String deleteGame(Integer id);

    /**
     * Method deletes all Games from repository if Game's quantity is equal to
     * 0.
     *
     * @return String confirmation of success and amount of deleted Games.
     */
    public String deleteAllUnstockGames();

}
