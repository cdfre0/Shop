package cdpr.web.service.impl;

import cdpr.web.exception.GameNotFoundException;
import cdpr.web.exception.IncorrectGameFormatException;
import cdpr.web.repository.GameRepository;
import cdpr.web.resources.Game;
import cdpr.web.service.GameService;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jan Michalec
 */
@Service
public class GameServiceImpl implements GameService {

    GameRepository repository;
    private static Integer idCounter = 0;
    
    private final Queue<Integer> deletedNumbers = new LinkedList<>();
    private final static String ID_NOT_EXIST
            = "Game with such ID does not exist in Repository";
    private final static String UPDATE_SUCCESS = "Update OK";
    private final static String DELETE_SUCCESS = "Delete OK";

    public GameServiceImpl(GameRepository repository) {
        this.repository = repository;
    }

    //CREATE
    @Override
    public String createGame(Game game) {
        if (game.getName() == null || game.getDeveloper() == null
                || game.getGenre() == null || game.getPrice() == 0.0) {
            throw new IncorrectGameFormatException(
                    "Format of game is incorrect");
        }

        for (Game existingGame : repository.findAll()) {
            if (existingGame.equals(game)) {
                return "Game already exist in Repository";
            }
        }

        if (deletedNumbers.isEmpty()) {
            game.setId(idCounter++);
        } else {
            game.setId(deletedNumbers.poll());
        }
        repository.save(game);
        return "Create OK";
    }

    //GET
    @Override
    public Game getGame(Integer id) {
        checkIdExisting(id);
        Game game = repository.findById(id).get();
        return game;

    }

    @Override
    public List<Game> getAllGames() {
        return repository.findAll();
    }

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

    @Override
    public List<Game> getGameByGenre(Game.Genre genre) {
        List<Game> games = new ArrayList<>();
        for (Game game : repository.findAll()) {
            if (game.getGenre().equals(genre)) {
                games.add(game);
            }
        }
        return games;
    }

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

    @Override
    public String isAvailable(Integer id) {
        checkIdExisting(id);
        Game game = repository.findById(id).get();
        if (game.getQuantity() > 0) {
            return "Game avaliable";
        }
        return "Game not avaliable";

    }

    //UPDATE
    @Override
    public String buyOneGame(Integer id) {
        checkIdExisting(id);
        Game game = repository.findById(id).get();
        if (game.getQuantity() > 0) {
            System.out.println(game.getQuantity());
            game.decreaseQuantity();
            System.out.println(game.getQuantity());
            repository.save(game);

            return game.getName() + " was bought for " + game.getPrice();
        }
        return "Not enought copies in stock";

    }

    @Override
    public String restockGame(Integer id, int quantity) {
        checkIdExisting(id);
        Game game = repository.findById(id).get();
        if (quantity < 1) {
            return "Quantity must be greater than 0 to restock";
        }
        System.out.println(game.getQuantity());
        game.restockQuantity(quantity);
        System.out.println(game.getQuantity());
        repository.save(game);
        return UPDATE_SUCCESS;

    }

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

    //DELETE
    @Override
    public String deleteGame(Integer id) {
        checkIdExisting(id);
        deletedNumbers.add(id);
        repository.deleteById(id);
        return DELETE_SUCCESS;

    }

    @Override
    public String deleteAllUnstockGames() {
        int deletedGames = 0;
        for (Game game : repository.findAll()) {
            if (game.getQuantity() < 1) {
                deletedNumbers.add(game.getId());
                repository.delete(game);
                deletedGames++;
            }
        }
        return DELETE_SUCCESS + " Deletions";
    }

    private void checkIdExisting(Integer id) {
        if (!repository.existsById(id)) {
            throw new GameNotFoundException(ID_NOT_EXIST);
        }
    }

}
