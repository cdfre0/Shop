package cdpr.web.controller;

import cdpr.web.resources.Game;
import cdpr.web.service.impl.GameServiceImpl;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Jan Michalec
 */
@RestController
@RequestMapping("/games")
public class GameController {

    private final String STRING_TO_INT_ERROR = "Cannot parse String to Integer";
    private final GameServiceImpl gameService;
   
    public GameController(GameServiceImpl gameService) {
        this.gameService = gameService;

        Game newGame = new Game("Heroes of Might & Magic V", "Nival", Game.Genre.STRATEGY, 100.0, 3);
        gameService.createGame(newGame);
        newGame = new Game("Resident Evil 4", "CAPCOM", Game.Genre.SIMULATION, 200.0, 5);
        gameService.createGame(newGame);
        newGame = new Game("The Great Ace Attorney Chronicles ", "CAPCOM", Game.Genre.STRATEGY, 120.0, 100);
        gameService.createGame(newGame);
    }

    //CREATE
    @PostMapping("admin")
    public String createGame(@RequestBody Game newGame) {
        
        return gameService.createGame(newGame);
    }

    //GETS
    @GetMapping("{stringId}")
    public Game getGameById(@PathVariable String stringId) {
        try {
            int id = Integer.parseInt(stringId);
            return gameService.getGame(id);
        } catch (NumberFormatException e) {
            System.out.println(STRING_TO_INT_ERROR);
            return null;
        }
    }

    @GetMapping("all")
    public List<Game> getAllGames() {
        return gameService.getAllGames();
    }

    //I know, controversial 
    @GetMapping("all/{variable}")
    public List<Game> getSpecificGames(@PathVariable String variable) {
        try {
            int price = Integer.parseInt(variable);
            return gameService.showStockLessThan(price);
        } catch (NumberFormatException e) {
            try {
                Game.Genre genre = Game.Genre.valueOf(variable);
                return gameService.getGameByGenre(genre);
            } catch (IllegalArgumentException ee) {
                return gameService.getGameByDev(variable);
            }
        }
    }

    @GetMapping("avaliable/{stringId}")
    public String isAvaliable(@PathVariable String stringId) {
        try {
            Integer id = Integer.parseInt(stringId);
            return gameService.isAvailable(id);
        } catch (NumberFormatException e) {
            System.out.println(STRING_TO_INT_ERROR);
            return null;
        }
    }

    @PutMapping
    public String buyOneGame(@RequestParam(name = "id") String stringId) {
        try {
            Integer id = Integer.parseInt(stringId);
            return gameService.buyOneGame(id);
        } catch (NumberFormatException e) {
            System.out.println(STRING_TO_INT_ERROR);
            return null;
        }
    }

    @PutMapping("admin")
    public String restockGame(@RequestParam(name = "id") String stringId,
            @RequestParam(name = "q") String stringQuantity) {
        try {
            Integer id = Integer.parseInt(stringId);
            int quantity = Integer.parseInt(stringQuantity);
            return gameService.restockGame(id, quantity);
        } catch (NumberFormatException e) {
            System.out.println(STRING_TO_INT_ERROR);
            return null;
        }
    }
    @PutMapping("admin/sale")
    public String putOnSale(@RequestParam(name = "id") String stringId,
            @RequestParam(name = "factor") String stringFactor){
        try {
            Integer id = Integer.parseInt(stringId);
            double factor = Double.parseDouble(stringFactor);
            return gameService.putGameOnSale(id, factor);
        } catch (NumberFormatException e) {
            System.out.println(STRING_TO_INT_ERROR);
            return null;
        }
    }
    
    //DELETE
    @DeleteMapping("admin/{stringId}")
    public String deleteGame(@PathVariable String stringId) {
        try {
            Integer id = Integer.parseInt(stringId);
            return gameService.deleteGame(id);
        } catch (NumberFormatException e) {
            System.out.println(STRING_TO_INT_ERROR);
            return null;
        }
    }
    @DeleteMapping("admin/clean")
    public String deleteUnstocked() {
        return gameService.deleteAllUnstockGames();
    }

}
