package cdpr.web.service.impl;

import cdpr.web.exception.GameNotFoundException;
import cdpr.web.repository.GameRepository;
import cdpr.web.resources.Game;
import cdpr.web.service.GameService;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.springframework.stereotype.Service;

/**
 * Class implements GAmeSerice interface. Used to communicate with Repository
 * and change it's instances.Supports CRUD methods. Supports multi threading by
 * locks.
 *
 * @author Jan Michalec
 */
@Service
public class GameServiceImpl implements GameService {

    /**
     * Final String for error of not existing ID in Repository.
     */
    private final static String ID_NOT_EXIST
            = "Game with such ID does not exist in Repository";
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
     * Lock for reading and writing data in database.
     */
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public GameServiceImpl(GameRepository repository) {
        this.repository = repository;
    }

    //CREATE
    /**
     * Method saves game in repository if it does not exist there.
     *
     * @param game Game to save
     * @return String information if success or error
     */
    @Override
    public String createGame(Game game) {
        lock.writeLock().lock();
        try {
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
        } finally {
            lock.writeLock().unlock();
        }
    }

    //GET
    /**
     * Method retrieves details of Game from repository given id.
     *
     * @param id Integer id of Game
     * @thows
     * @return Game instance or error message
     */
    @Override
    public Game getGame(Integer id) {
        lock.readLock().lock();
        try {
            checkIdExisting(id);
            Game game = repository.findById(id).get();
            return game;
        } finally {
            lock.readLock().unlock();
        }

    }

    /**
     * Method retrieves details of all Games that has given name.
     *
     * @param name String name to check
     * @return List of Games
     */
    @Override
    public List<Game> findGameByName(String name) {

        lock.readLock().lock();
        try {
            return repository.findAllByName(name);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Method retrieves details of all games from repository.
     *
     * @return List of Games
     */
    @Override
    public List<Game> getAllGames() {
        lock.readLock().lock();
        try {
            return repository.findAll();
        } finally {
            lock.readLock().unlock();
        }
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
        lock.readLock().lock();
        try {
            for (Game game : repository.findAll()) {
                if (game.getDeveloper().equals(developerName)) {
                    games.add(game);
                }
            }
            return games;
        } finally {
            lock.readLock().unlock();
        }
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
        lock.readLock().lock();
        try {
            for (Game game : repository.findAll()) {
                for (Game.Genre existingGenre : game.getGenres()) {
                    if (existingGenre.equals(genre)) {
                        games.add(game);
                    }
                }
            }
            return games;
        } finally {
            lock.readLock().unlock();
        }
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
        lock.readLock().lock();
        try {
            for (Game game : repository.findAll()) {
                if (game.getPrice() < price) {
                    games.add(game);
                }
            }
            return games;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Method checks if Game has any copies in repository, given id.
     *
     * @param id Integer id of Game
     * @return boolean true if exist
     */
    @Override
    public boolean isAvailable(Integer id) {
        lock.readLock().lock();
        try {
            checkIdExisting(id);
            Game game = repository.findById(id).get();
            if (game.getQuantity() > 0) {
                return true;
            }
            return false;
        } finally {
            lock.readLock().unlock();
        }

    }

    //UPDATE
    /**
     * Method decreases Quantity of Games Quantity, given it's id.
     *
     * @param id Integer id of Game
     * @return String Confirmation of success or error
     */
    @Override
    public String buyOneGame(Integer id) {
        lock.writeLock().lock();
        try {
            checkIdExisting(id);
            Game game = repository.findById(id).get();
            if (game.getQuantity() > 0) {
                game.decreaseQuantity();
                repository.save(game);

                return game.getName() + " was bought for " + game.getPrice();
            }
            return "Not enought copies in stock";
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Method adds quantity of Game given number to add and game's id.
     *
     * @param id Integer id of Game
     * @param quantity int number to add
     * @return String Confirmation of success or error
     */
    @Override
    public String restockGame(Integer id, int quantity) {
        lock.writeLock().lock();
        try {
            checkIdExisting(id);
            Game game = repository.findById(id).get();
            if (quantity < 1) {
                return "Quantity must be greater than 0 to restock";
            }
            game.restockQuantity(quantity);
            repository.save(game);
            return UPDATE_SUCCESS;
        } finally {
            lock.writeLock().unlock();
        }

    }

    /**
     * Method changes price of Game given factor to multiply price and game's
     * id.
     *
     * @param id Integer id of Game
     * @param factor double factor that multiplies price
     * @return String Confirmation of success or error
     */
    @Override
    public String putGameOnSale(Integer id, double factor) {
        if (factor == 0) {
            return "Factor cannot be equal to 0";
        }
        lock.writeLock().lock();
        try {
            checkIdExisting(id);
            Game game = repository.findById(id).get();
            game.setOnSale(factor);
            repository.save(game);
            return UPDATE_SUCCESS;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Method adds genre to Game's list of genre, given game's id and genre.
     *
     * @param id Integer id of Game
     * @param genre Genre type
     * @return String Confirmation of success or error
     */
    @Override
    public String addGenreToGame(Integer id, Game.Genre genre) {

        lock.writeLock().lock();
        try {
            checkIdExisting(id);
            Game game = repository.findById(id).get();
            if (game.hasGenre(genre)) {
                return UPDATE_FAIL + ", game has such genre already.";
            }
            game.addGenre(genre);
            repository.save(game);
            return UPDATE_SUCCESS;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Method deletes genre from Game's list of genre, given game's id and
     * genre.
     *
     * @param id Integer id of Game
     * @param genre Genre type
     * @return String Confirmation of success or error
     */
    @Override
    public String deleteGenreFromGame(Integer id, Game.Genre genre) {

        lock.writeLock().lock();
        try {
            checkIdExisting(id);
            Game game = repository.findById(id).get();
            if (!game.hasGenre(genre)) {
                return UPDATE_FAIL + ", game does not have such genre.";
            }
            if(game.getGenres().size() < 2){
                return UPDATE_FAIL + ", game needs to have at least 1 genre.";
            }
            game.deleteGenre(genre);
            repository.save(game);
            return UPDATE_SUCCESS;
        } finally {
            lock.writeLock().unlock();
        }
    }

    //DELETE
    /**
     * Method deletes Game from repository given Game's id.
     *
     * @param id Integer id of Game
     * @return Confirmation of success or error
     */
    @Override
    public String deleteGame(Integer id) {

        lock.writeLock().lock();
        try {
            checkIdExisting(id);
            deletedIds.add(id);
            repository.deleteById(id);
            return DELETE_SUCCESS;
        } finally {
            lock.writeLock().unlock();
        }

    }

    /**
     * Method deletes all Games from repository if Game's quantity is equal to
     * 0.
     *
     * @return String Confirmation of success and amount of deleted Games.
     */
    @Override
    public String deleteAllUnstockGames() {
        int deletedGames = 0;
        lock.writeLock().lock();
        try {
            for (Game game : repository.findAll()) {
                if (game.getQuantity() < 1) {
                    deletedIds.add(game.getId());
                    repository.delete(game);
                    deletedGames++;
                }
            }
            return DELETE_SUCCESS + " " + deletedGames;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Method Checks if Game of given id exist in repository.
     *
     *
     * @param id Integer id of Game
     * @throws GameNotFoundException if does not exist.
     */
    private void checkIdExisting(Integer id) {
        if (!repository.existsById(id)) {
            throw new GameNotFoundException(ID_NOT_EXIST);
        }
    }

}
