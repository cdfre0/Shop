package cdpr.web.controller;

import cdpr.web.resources.Game;
import cdpr.web.responce.ResponseHandler;
import cdpr.web.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Jan Michalec
 */
@RestController
public class GameController {

    private static final String STRING_TO_INT_ERROR = "Cannot parse String to Integer";
    private static final String DATA_COLLECTED = "Data collected";
    private static final String DATA_UPDATED = "Data updated";
    private static final String DATA_DELETED = "Data deleted";
    private final GameService gameService;

    public GameController(GameService gameService) {
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
    public ResponseEntity<Object> createGame(@RequestBody Game newGame) {
        if (newGame.getName() == null || newGame.getDeveloper() == null
                || newGame.getGenre() == null || newGame.getPrice() == 0.0) {
            return ResponseHandler.responseBuilder("Wrong format of game"
                    , HttpStatus.UNSUPPORTED_MEDIA_TYPE, null);
        }
        return ResponseHandler.responseBuilder("Game put in repository",
                HttpStatus.CREATED, gameService.createGame(newGame));
    }

    //GETS
    @GetMapping("{stringId}")
    public ResponseEntity<Object> getGameById(@PathVariable String stringId) {
        try {
            int id = Integer.parseInt(stringId);
            return ResponseHandler.responseBuilder(DATA_COLLECTED,
                    HttpStatus.OK, gameService.getGame(id));
        } catch (NumberFormatException e) {
            return ResponseHandler.responseBuilder(STRING_TO_INT_ERROR,
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
    }

    @GetMapping("all")
    public ResponseEntity<Object> getAllGames() {
        return ResponseHandler.responseBuilder(DATA_COLLECTED,
                    HttpStatus.OK, gameService.getAllGames());
    }

    //I know, controversial 
    @GetMapping("all/{variable}")
    public ResponseEntity<Object> getSpecificGames(@PathVariable String variable) {
        try {
            int price = Integer.parseInt(variable);
            return ResponseHandler.responseBuilder(DATA_COLLECTED,
                    HttpStatus.OK, gameService.showStockLessThan(price));
        } catch (NumberFormatException e) {
            try {
                Game.Genre genre = Game.Genre.valueOf(variable);
                return ResponseHandler.responseBuilder(DATA_COLLECTED,
                    HttpStatus.OK, gameService.getGameByGenre(genre));
            } catch (IllegalArgumentException ee) {
                return ResponseHandler.responseBuilder(DATA_COLLECTED,
                    HttpStatus.OK, gameService.getGameByDev(variable));
            }
        }
    }

    @GetMapping("avaliable/{stringId}")
    public ResponseEntity<Object> isAvaliable(@PathVariable String stringId) {
        try {
            Integer id = Integer.parseInt(stringId);
            return ResponseHandler.responseBuilder(DATA_COLLECTED,
                    HttpStatus.OK, gameService.isAvailable(id));
        } catch (NumberFormatException e) {
            return ResponseHandler.responseBuilder(STRING_TO_INT_ERROR,
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
    }

    @PutMapping("{stringId}")
    public ResponseEntity<Object> buyOneGame(@PathVariable String stringId) {
        try {
            Integer id = Integer.parseInt(stringId);
            return ResponseHandler.responseBuilder(DATA_UPDATED,
                    HttpStatus.OK, gameService.buyOneGame(id));
        } catch (NumberFormatException e) {
            return ResponseHandler.responseBuilder(STRING_TO_INT_ERROR,
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
    }

    @PutMapping("admin")
    public ResponseEntity<Object> restockGame(@RequestParam(name = "id") String stringId,
            @RequestParam(name = "q") String stringQuantity) {
        try {
            Integer id = Integer.parseInt(stringId);
            int quantity = Integer.parseInt(stringQuantity);
            return ResponseHandler.responseBuilder(DATA_UPDATED,
                    HttpStatus.OK, gameService.restockGame(id, quantity));
        } catch (NumberFormatException e) {
            return ResponseHandler.responseBuilder(STRING_TO_INT_ERROR,
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
    }

    @PutMapping("admin/sale")
    public ResponseEntity<Object> putOnSale(@RequestParam(name = "id") String stringId,
            @RequestParam(name = "factor") String stringFactor) {
        try {
            Integer id = Integer.parseInt(stringId);
            double factor = Double.parseDouble(stringFactor);
            return ResponseHandler.responseBuilder(DATA_UPDATED,
                    HttpStatus.OK, gameService.putGameOnSale(id, factor));
        } catch (NumberFormatException e) {
            return ResponseHandler.responseBuilder(STRING_TO_INT_ERROR,
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
    }

    //DELETE
    @DeleteMapping("admin/{stringId}")
    public ResponseEntity<Object> deleteGame(@PathVariable String stringId) {
        try {
            Integer id = Integer.parseInt(stringId);
            return ResponseHandler.responseBuilder(DATA_DELETED,
                    HttpStatus.OK, gameService.deleteGame(id));
        } catch (NumberFormatException e) {
            return ResponseHandler.responseBuilder(STRING_TO_INT_ERROR,
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
    }

    @DeleteMapping("admin/clean")
    public ResponseEntity<Object> deleteUnstocked() {
        return ResponseHandler.responseBuilder(DATA_DELETED,
                    HttpStatus.OK, gameService.deleteAllUnstockGames());
    }

}
