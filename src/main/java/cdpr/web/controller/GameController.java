package cdpr.web.controller;

import cdpr.web.resources.Game;
import cdpr.web.response.ResponseHandler;
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
 * Class GameController controls flow of calls between http calls and server.
 * Checks if calls are correctly build. Makes calls to Service to retrieve data
 * and send them in ResponseEntity.
 *
 * @author Jan Michalec
 */
@RestController
public class GameController {

    /**
     * Final String for error of failed cased String to Integer.
     */
    private static final String STRING_TO_INT_ERROR = "Cannot parse String to Integer";
    /**
     * Final String to indicate data was successfully collected from repository.
     */
    private static final String DATA_COLLECTED = "Data collected";
    /**
     * Final String to indicate data was successfully updated in repository.
     */
    private static final String DATA_UPDATED = "Data updated";
    /**
     * Final String to indicate data was successfully deleted from repository.
     */
    private static final String DATA_DELETED = "Data deleted";
    /**
     * Game service instance to communicate with repository.
     */
    private final GameService gameService;

    /**
     * Constructor initiates gameService and put some values in repository.
     *
     * @param gameService GameService to communicate
     */
    public GameController(GameService gameService) {
        this.gameService = gameService;

        Game newGame = new Game("Heroes of Might & Magic V", "Nival",
                Game.Genre.STRATEGY, 100.0, 3);
        gameService.createGame(newGame);
        newGame = new Game("Resident Evil 4", "CAPCOM",
                Game.Genre.SIMULATION, 200.0, 5);
        gameService.createGame(newGame);
        newGame = new Game("The Great Ace Attorney Chronicles ",
                "CAPCOM", Game.Genre.STRATEGY, 120.0, 100);
        gameService.createGame(newGame);
    }

    //CREATE
    /**
     * Method calls gameService to put Game in repository, if data is input
     * correctly.
     *
     * @param newGame Game instance of game
     * @return Confirmation of success or error
     */
    @PostMapping("admin")
    public ResponseEntity<Object> createGame(@RequestBody Game newGame) {
        if (newGame.getName() == null || newGame.getDeveloper() == null
                || newGame.getGenre() == null || newGame.getPrice() == 0.0) {
            return ResponseHandler.responseBuilder("Wrong format of game",
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, null);
        }
        return ResponseHandler.responseBuilder("Game put in repository",
                HttpStatus.CREATED, gameService.createGame(newGame));
    }

    //GETS
    /**
     * Method calls gameService to retrieve data of game of given id. Checks if
     * id is an int.
     *
     * @param stringId String should contain number
     * @return Confirmation of success or error
     */
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

    /**
     * Method calls gameService to retrieve data of all games.
     *
     * @return Confirmation of success or error
     */
    @GetMapping("all")
    public ResponseEntity<Object> getAllGames() {
        return ResponseHandler.responseBuilder(DATA_COLLECTED,
                HttpStatus.OK, gameService.getAllGames());
    }

    //I know, controversial 
    /**
     * Method calls gameService to retrieve data under specific conditions. If
     * is a number, it looks for games of value less than it. If is type of
     * Genre, looks for games of this Genre. Otherwise, looks for games of this
     * developer.
     *
     * @param variable String variable with condition
     * @return Confirmation of success or error
     */
    //I am aware it should be 3 methods.
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

    /**
     * Method calls gameSerive to check if game has any copies in store given
     * it's id.
     *
     * @param stringId String should contain number
     * @return Confirmation of success or error
     */
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

    /**
     * Method calls gameService to buy one copy of game given it's id. Decreases
     * number of game stock by 1.
     *
     * @param stringId String should contain number
     * @return Confirmation of success or error
     */
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

    /**
     * Method calls gameSerivce to add stock of game by it's id.
     *
     * @param stringId String should contain number
     * @param stringQuantity String should contain number greater than 0
     * @return Confirmation of success or error
     */
    @PutMapping("admin")
    public ResponseEntity<Object> restockGame(@RequestParam(name = "id") String stringId,
            @RequestParam(name = "quantity") String stringQuantity) {
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

    /**
     * Method calls gameService to change price of game multiplying it's price
     * by a factor. Game fetched by it's id.
     *
     * @param stringId String should contain number
     * @param stringFactor String should contain double
     * @return Confirmation of success or error
     */
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
    /**
     * Method calls GameService to delete game by it's id.
     *
     * @param stringId String should contain number
     * @return Confirmation of success or error
     */
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

    /**
     * Method calls GameService to delete games that has 0 copies in repository.
     *
     * @return Confirmation of success or error
     */
    @DeleteMapping("admin/clean")
    public ResponseEntity<Object> deleteUnstocked() {
        return ResponseHandler.responseBuilder(DATA_DELETED,
                HttpStatus.OK, gameService.deleteAllUnstockGames());
    }

}
