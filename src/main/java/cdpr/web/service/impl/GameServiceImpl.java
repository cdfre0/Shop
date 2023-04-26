package cdpr.web.service.impl;

import cdpr.web.exception.ObjectNotFoundException;
import cdpr.web.repository.GameRepository;
import cdpr.web.resources.Game;
import cdpr.web.service.GameService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.springframework.stereotype.Service;

/**
 * Class implements GameSerice interface. Used to communicate with Repository of
 * Games and change it's instances.Supports CRUD methods. Supports multi
 * threading by locks.
 *
 * @author Jan Michalec
 */
@Service
public class GameServiceImpl implements GameService {

    /**
     * Final String to indicate Update success.
     */
    private final static String UPDATE_SUCCESS = "Update OK";
    /**
     * Final String to indicate failure of Update.
     */
    private final static String UPDATE_FAIL = "Update Failed";
    /**
     * Final String to indicate Deletion success.
     */
    private final static String DELETE_SUCCESS = "Delete OK";
    /**
     * Counter of IDs.
     */
    private static Integer idCounter = 0;
    /**
     * Queue of deleted ids for reuse.
     */
    private final Queue<Integer> deletedIds = new LinkedList<>();
    /**
     * Repository for saving data.
     */
    GameRepository repository;

    /**
     * Populating repository on start.
     *
     * @param repository GameRepository, repository this Service will be using
     */
    public GameServiceImpl(GameRepository repository) {
        this.repository = repository;
        Game newGame = new Game("Heroes of Might & Magic V", "Nival",
                Arrays.asList(Game.Genre.STRATEGY), 100.0, 3);
        createGame(newGame);
        newGame = new Game("Resident Evil 4", "CAPCOM",
                Arrays.asList(Game.Genre.HORROR, Game.Genre.ACTION), 200.0, 5);
        createGame(newGame);
        newGame = new Game("The Great Ace Attorney Chronicles ",
                "CAPCOM", Arrays.asList(Game.Genre.STRATEGY), 120.0, 100);
        createGame(newGame);
    }

    //CREATE
    /**
     * Method saves game in repository if it does not exist there.
     *
     * @param game Game to save
     * @return String information of success or error
     */
    @Override
    public String createGame(Game game) {

        for (Game existingGame : repository.findAll()) {
            if (existingGame.equals(game)) {
                return "Game already exist in Repository";
            }
        }

        if (deletedIds.isEmpty()) {
            game.setId(idCounter++);
        } else {
            game.setId(deletedIds.poll());
        }
        repository.save(game);
        return "Create OK";
    }

    //GET
    /**
     * Method retrieves details of Game from repository given id.
     *
     * @param id Integer id of Game
     * @return Game instance or error message
     */
    @Override
    public Game getGame(Integer id) {
        checkIdExisting(id);
        Game game = repository.findById(id).get();
        return game;
    }

    /**
     * Method retrieves details of all Games that has given name.
     *
     * @param name String name to check
     * @return List of Games
     */
    @Override
    public List<Game> findGameByName(String name) {
        return repository.findAllByName(name);
    }

    /**
     * Method retrieves details of all games from repository.
     *
     * @return List of Games
     */
    @Override
    public List<Game> getAllGames() {
        return repository.findAll();
    }

    /**
     * Method retrieves details of games containing given developer name.
     *
     * @param developerName String developerName to check in Game instance
     * @return List of Games
     */
    @Override
    public List<Game> getGameByDev(String developerName) {
        List<Game> games = new ArrayList<>();
        for (Game game : repository.findAll()) {
            if (game.getDeveloper().equals(developerName)) {
                games.add(game);
            }
        }
        return games;
    }

    /**
     * Method retrieves details of games containing given Genre type.
     *
     * @param genre Genre type to check in Game instance
     * @return List of Games
     */
    @Override
    public List<Game> getGameByGenre(Game.Genre genre) {
        List<Game> games = new ArrayList<>();
        for (Game game : repository.findAll()) {
            for (Game.Genre existingGenre : game.getGenres()) {
                if (existingGenre.equals(genre)) {
                    games.add(game);
                }
            }
        }
        return games;
    }

    /**
     * Method retrieves details of games that price is less than given.
     *
     * @param price double an upper boundary to check Games' price
     * @return List of Games
     */
    @Override
    public List<Game> showStockLessThan(double price) {
        List<Game> games = new ArrayList<>();
        for (Game game : repository.findAll()) {
            if (game.getPrice() < price) {
                games.add(game);
            }
        }
        return games;
    }

    /**
     * Method checks if Game has any copies in repository, given id.
     *
     * @param id Integer id of Game
     * @return boolean true if exist
     */
    @Override
    public boolean isAvailable(Integer id) {
        checkIdExisting(id);
        Game game = repository.findById(id).get();
        if (game.getQuantity() > 0) {
            return true;
        }
        return false;
    }

    //UPDATE
    /**
     * Method decreases Quantity of Games Quantity, given it's id.
     *
     * @param id Integer id of Game
     * @return String confirmation of success or error
     */
    @Override
    public String buyOneGame(Integer id) {
        checkIdExisting(id);
        Game game = repository.findById(id).get();
        if (game.getQuantity() > 0) {
            game.decreaseQuantity();
            repository.save(game);

            return game.getName() + " was bought for " + game.getPrice();
        }
        return "Not enought copies in stock";
    }

    /**
     * Method adds quantity of Game given number to add and game's id.
     *
     * @param id Integer id of Game
     * @param quantity int number to add
     * @return String confirmation of success or error
     */
    @Override
    public String restockGame(Integer id, int quantity) {
        checkIdExisting(id);
        Game game = repository.findById(id).get();
        if (quantity < 1) {
            return "Quantity must be greater than 0 to restock";
        }
        game.restockQuantity(quantity);
        repository.save(game);
        return UPDATE_SUCCESS;
    }

    /**
     * Method changes name of Game to new one.
     *
     * @param id Integer id of game name to change
     * @param name String new name
     * @return String confirmation of success or error
     */
    @Override
    public String changeName(Integer id, String name) {
        checkIdExisting(id);
        Game game = repository.findById(id).get();
        game.setName(name);
        repository.save(game);
        return UPDATE_SUCCESS;
    }

    /**
     * Method changes developer name of Game to new one.
     *
     * @param id Integer id of game developer name to change
     * @param developer String new developer name
     * @return String confirmation of success or error
     */
    @Override
    public String changeDeveloper(Integer id, String developer) {
        checkIdExisting(id);
        Game game = repository.findById(id).get();
        game.setDeveloper(developer);
        repository.save(game);
        return UPDATE_SUCCESS;
    }

    /**
     * Method changes game stored at specific id to new one.
     *
     * @param id Integer id of game to change
     * @param game Game new game
     * @return String confirmation of success or error
     */
    @Override
    public String rewriteGame(Integer id, Game game) {
        checkIdExisting(id);
        game.setId(id);
        repository.save(game);
        return UPDATE_SUCCESS;
    }

    /**
     * Method changes price of Game given factor to multiply price and game's
     * id.
     *
     * @param id Integer id of Game
     * @param factor double factor that multiplies price
     * @return String confirmation of success or error
     */
    @Override
    public String putGameOnSale(Integer id, double factor) {
        if (factor == 0) {
            return "Factor cannot be equal to 0";
        }
        checkIdExisting(id);
        Game game = repository.findById(id).get();
        game.setOnSale(factor);
        repository.save(game);
        return UPDATE_SUCCESS;
    }

    /**
     * Method adds genre to Game's list of genre, given game's id and genre.
     *
     * @param id Integer id of Game
     * @param genre Genre type
     * @return String confirmation of success or error
     */
    @Override
    public String addGenreToGame(Integer id, Game.Genre genre) {
        checkIdExisting(id);
        Game game = repository.findById(id).get();
        if (game.hasGenre(genre)) {
            return UPDATE_FAIL + ", game has such genre already.";
        }
        game.addGenre(genre);
        repository.save(game);
        return UPDATE_SUCCESS;
    }

    /**
     * Method deletes genre from Game's list of genre, given game's id and
     * genre.
     *
     * @param id Integer id of Game
     * @param genre Genre type
     * @return String confirmation of success or error
     */
    @Override
    public String deleteGenreFromGame(Integer id, Game.Genre genre) {
        checkIdExisting(id);
        Game game = repository.findById(id).get();
        if (!game.hasGenre(genre)) {
            return UPDATE_FAIL + ", game does not have such genre.";
        }
        if (game.getGenres().size() < 2) {
            return UPDATE_FAIL + ", game needs to have at least 1 genre.";
        }
        game.deleteGenre(genre);
        repository.save(game);
        return UPDATE_SUCCESS;
    }

    //DELETE
    /**
     * Method deletes Game from repository given Game's id.
     *
     * @param id Integer id of Game
     * @return String confirmation of success or error
     */
    @Override
    public String deleteGame(Integer id) {
        checkIdExisting(id);
        deletedIds.add(id);
        repository.deleteById(id);
        return DELETE_SUCCESS;
    }

    /**
     * Method deletes all Games from repository if Game's quantity is equal to
     * 0.
     *
     * @return String confirmation of success and amount of deleted Games.
     */
    @Override
    public String deleteAllUnstockGames() {
        int deletedGames = 0;

        for (Game game : repository.findAll()) {
            if (game.getQuantity() < 1) {
                deletedIds.add(game.getId());
                repository.delete(game);
                deletedGames++;
            }
        }
        return DELETE_SUCCESS + " " + deletedGames;
    }

    /**
     * Method Checks if Game of given id exist in repository.
     * //throws GameNotFoundException if does not exist.
     *
     * @param id Integer id of Game
     */
    private void checkIdExisting(Integer id) {
        if (!repository.existsById(id)) {
            throw new ObjectNotFoundException(
                    "Game with such ID does not exist in Repository");
        }
    }

}
