package cdpr.web.service.impl;

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
   
    public GameServiceImpl(GameRepository repository) {
        this.repository = repository;
    }

    //CREATE
    @Override
    public String createGame(Game game) {
        if(game.getName() == null || game.getDeveloper() == null 
                || game.getGenre() == null || game.getPrice() == 0.0) {
            return "Bad format of game";
        }

        for (Game existingGame : repository.findAll()) {
            if (existingGame.equals(game)) {
                return "Game already exist in Repository";
            }
        }
        
         if(deletedNumbers.isEmpty()){
            game.setId(idCounter++);
        }else {
            game.setId(deletedNumbers.poll());
        }
        repository.save(game);
        return "Create OK";
    }

    //GET
    @Override
    public Game getGame(Integer id) {
        Game game = repository.findById(id).get();
        return game;
        //TODO Problem
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

        Game game = repository.findById(id).get();
        if (game != null) {
            if (game.getQuantity() > 0) {
                return "Game avaliable";
            }
            return "Game not avaliable";
        }
        return "Game with this id does not exist";
    }

    //UPDATE
    @Override
    public String buyOneGame(Integer id) {
        Game game = repository.findById(id).get();
        if (game != null) {
            if (game.getQuantity() > 0) {
                game.decreaseQuantity();
                return game.getName() + " was bought for " + game.getPrice();
            }
            return "Not enought copies in stock";

        }
        return "Game with this id does not exist";
    }

    @Override
    public String restockGame(Integer id, int quantity) {
        
        Game game = repository.findById(id).get();
        if (game != null) {
            if(quantity < 1){
            return "Quantity must be greater than 0 to restock";
            }
            game.restockQuantity(quantity);
            return "Update OK";
        }
        return "Game wuth this id does not exist";
    }

    @Override
    public String putGameOnSale(Integer id, double factor) {
        Game game = repository.findById(id).get();
        if (game != null) {
            game.setOnSale(factor);
            return "Update OK";
        }
        return "Game wuth this id does not exist";
    }

    //DELETE
    @Override
    public String deleteGame(Integer id) {
        if (repository.existsById(id)) {
            deletedNumbers.add(id);
            repository.deleteById(id);
            return "Delete OK";
        }
        return "Game does not exist in Repository";
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
        return "Delete OK " + deletedGames + " Deletions";
    }

}
