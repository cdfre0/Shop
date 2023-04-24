package cdpr.web.controller;

import cdpr.web.resources.Game;
import cdpr.web.response.ResponseHandler;
import cdpr.web.service.GameService;
import java.util.Arrays;
import java.util.List;
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
     * Final String for error of failed cast String to Integer.
     */
    private static final String STRING_TO_INT_ERROR = "Cannot parse String to Integer";
    /**
     * Final String for error of failed cast String to Genre type
     */
    private static final String GENRE_INCORRECT = "Genre type incorrect";

    /**
     * Final String to indicate call successfully reached service.
     */
    private static final String CALL_REACHED = "Call reached server";

    private static final String EMPTY_SET = "Retured Collection is empty";

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
                Arrays.asList(Game.Genre.STRATEGY), 100.0, 3);
        gameService.createGame(newGame);
        newGame = new Game("Resident Evil 4", "CAPCOM",
                Arrays.asList(Game.Genre.HORROR, Game.Genre.ACTION), 200.0, 5);
        gameService.createGame(newGame);
        newGame = new Game("The Great Ace Attorney Chronicles ",
                "CAPCOM", Arrays.asList(Game.Genre.STRATEGY), 120.0, 100);
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
                || newGame.getGenres() == null || newGame.getPrice() == 0.0) {
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
     * @return Confirmation of success with Games or error
     */
    @GetMapping("byId/{stringId}")
    public ResponseEntity<Object> getGameById(@PathVariable String stringId) {
        try {
            int id = Integer.parseInt(stringId);
            return ResponseHandler.responseBuilder(CALL_REACHED,
                    HttpStatus.OK, gameService.getGame(id));
        } catch (NumberFormatException e) {
            return ResponseHandler.responseBuilder(STRING_TO_INT_ERROR,
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
    }

    /**
     * Method calls gameService to retrieve data of all games of given name.
     *
     * @param name String name to check
     * @return Confirmation of success with List of Games or error
     */
    @GetMapping("{name}")
    public ResponseEntity<Object> getGameByName(@PathVariable String name) {
        List<Game> list = gameService.findGameByName(name);
        if (list.isEmpty()) {
            return ResponseHandler.responseBuilder(EMPTY_SET,
                    HttpStatus.BAD_REQUEST, null);
        }
        return ResponseHandler.responseBuilder(CALL_REACHED,
                HttpStatus.OK, list);
    }

    /**
     * Method calls gameService to retrieve data of all games.
     *
     * @return Confirmation of success with List of Games or error
     */
    @GetMapping("all")
    public ResponseEntity<Object> getAllGames() {
        List<Game> list = gameService.getAllGames();
        if (list.isEmpty()) {
            return ResponseHandler.responseBuilder(EMPTY_SET,
                    HttpStatus.BAD_REQUEST, null);
        }
        return ResponseHandler.responseBuilder(CALL_REACHED,
                HttpStatus.OK, list);
    }

    //I know, controversial 
    /**
     * Method calls gameService to retrieve data under specific conditions. If
     * is a number, it looks for games of value less than it. If is type of
     * Genre, looks for games of this Genre. Otherwise, looks for games of this
     * developer.
     *
     * @param variable String variable with condition
     * @return Confirmation of success with List of Games or error
     */
    //I am aware it should be 3 methods.
    @GetMapping("all/{variable}")
    public ResponseEntity<Object> getSpecificGames(@PathVariable String variable) {
        List<Game> list;
        try {
            int price = Integer.parseInt(variable);
            list = gameService.showStockLessThan(price);
            if (list.isEmpty()) {
                return ResponseHandler.responseBuilder(EMPTY_SET,
                        HttpStatus.BAD_REQUEST, null);
            }
            return ResponseHandler.responseBuilder(CALL_REACHED,
                    HttpStatus.OK, list);
        } catch (NumberFormatException e) {
            try {
                Game.Genre genre = Game.Genre.valueOf(variable);
                list = gameService.getGameByGenre(genre);
                if (list.isEmpty()) {
                    return ResponseHandler.responseBuilder(EMPTY_SET,
                            HttpStatus.BAD_REQUEST, null);
                }
                return ResponseHandler.responseBuilder(CALL_REACHED,
                        HttpStatus.OK, list);
            } catch (IllegalArgumentException ee) {
                list = gameService.getGameByDev(variable);
                if (list.isEmpty()) {
                    return ResponseHandler.responseBuilder(EMPTY_SET,
                            HttpStatus.BAD_REQUEST, null);
                }
                return ResponseHandler.responseBuilder(CALL_REACHED,
                        HttpStatus.OK, list);
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
            return ResponseHandler.responseBuilder(CALL_REACHED,
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
            return ResponseHandler.responseBuilder(CALL_REACHED,
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
            return ResponseHandler.responseBuilder(CALL_REACHED,
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
        Integer id;
        try {
            id = Integer.parseInt(stringId);
        } catch (NumberFormatException e) {
            return ResponseHandler.responseBuilder(STRING_TO_INT_ERROR,
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
        double factor = Double.parseDouble(stringFactor);
        return ResponseHandler.responseBuilder(CALL_REACHED,
                HttpStatus.OK, gameService.putGameOnSale(id, factor));

    }

    /**
     * Method calls gameService to add genre to game's list of genre. Game
     * fetched by it's id.
     *
     * @param stringId String should contain number
     * @param stringGenre String should contain Genre name
     * @return Confirmation of success or error
     */
    @PutMapping("admin/addGenre")
    public ResponseEntity<Object> addGenreToGame(@RequestParam(name = "id") String stringId,
            @RequestParam(name = "genre") String stringGenre) {
        Integer id;
        try {
            id = Integer.parseInt(stringId);
        } catch (NumberFormatException e) {
            return ResponseHandler.responseBuilder(STRING_TO_INT_ERROR,
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
        Game.Genre genre;
        try {
            genre = Game.Genre.valueOf(stringGenre);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder(GENRE_INCORRECT,
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
        return ResponseHandler.responseBuilder(CALL_REACHED,
                HttpStatus.OK, gameService.addGenreToGame(id, genre));
    }

    /**
     * Method calls gameService to delete genre from game's list of genre. Game
     * fetched by it's id.
     *
     * @param stringId String should contain number
     * @param stringGenre String should contain Genre name
     * @return Confirmation of success or error
     */
    @PutMapping("admin/deleteGenre")
    public ResponseEntity<Object> deleteGenreFromGame(@RequestParam(name = "id") String stringId,
            @RequestParam(name = "genre") String stringGenre) {
        Integer id;
        try {
            id = Integer.parseInt(stringId);
        } catch (NumberFormatException e) {
            return ResponseHandler.responseBuilder(STRING_TO_INT_ERROR,
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
        Game.Genre genre;
        try {
            genre = Game.Genre.valueOf(stringGenre);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder(GENRE_INCORRECT,
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
        return ResponseHandler.responseBuilder(CALL_REACHED,
                HttpStatus.OK, gameService.deleteGenreFromGame(id, genre));
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
            return ResponseHandler.responseBuilder(CALL_REACHED,
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
        return ResponseHandler.responseBuilder(CALL_REACHED,
                HttpStatus.OK, gameService.deleteAllUnstockGames());
    }

}
