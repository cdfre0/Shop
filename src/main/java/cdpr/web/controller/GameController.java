package cdpr.web.controller;

import cdpr.web.resources.Game;
import cdpr.web.resources.User;
import cdpr.web.response.ResponseHandler;
import cdpr.web.service.GameService;
import cdpr.web.service.UserService;
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
 * Checks if calls are correctly build. Checks if user calling method have
 * appropriate permissions. Makes calls to Service to retrieve data and send
 * them in ResponseEntity.
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
    /**
     * Final String to indicate returned collection is empty.
     */
    private static final String EMPTY_COLLECTION = "Retured Collection is empty";

    /**
     * Final String to indicate user is already logged.
     */
    private static final String LOGGED_IN = "You are already logged in";
    /**
     * Final String to indicate user is not yet logged in.
     */
    private static final String NOT_LOGGED_IN = "You are not logged in";
    /**
     * Final String to indicate used does not have permissions.
     */
    private static final String NOT_ENOGUHT_PERMISSION
            = "You do not have enought permissions to make this call";
    /**
     * Game service instance to communicate with repository of games.
     */
    private final GameService gameService;
    /**
     * User service instance to communicate with repository of users.
     */
    private final UserService userService;
    /**
     * Current profile of user using Controller.
     */
    private User currentUser = null;

    /**
     * Constructor initiates Game Service and User Service.
     *
     * @param gameService GameService to communicate with
     * @param userService UserService to communicate with
     */
    public GameController(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    ////////////////////USER CALLS//////////////////////////////////////////////
    //CREATE
    /**
     * Method calls userService to put User in repository, if data is input
     * correctly and no user is logged in.
     *
     * @param newUser User instance of user
     * @return Confirmation of success or error
     */
    @PostMapping("create")
    public ResponseEntity<Object> createUser(@RequestBody User newUser) {
        if (currentUser != null) {
            return ResponseHandler.responseBuilder(LOGGED_IN,
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
        if (newUser.getLogin() == null || newUser.getPassword() == null) {
            return ResponseHandler.responseBuilder("Wrong format of user",
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, null);
        }
        return ResponseHandler.responseBuilder(CALL_REACHED,
                HttpStatus.CREATED, userService.addUser(newUser));
    }

    /**
     * Method sets current user to give, if exist in userService.
     *
     * @param newUser User instance of user
     * @return Confirmation of success or error
     */
    @PostMapping("login")
    public ResponseEntity<Object> logIn(@RequestBody User newUser) {
        if (currentUser != null) {
            return ResponseHandler.responseBuilder(LOGGED_IN,
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
        if (newUser.getLogin() == null || newUser.getPassword() == null) {
            return ResponseHandler.responseBuilder("Wrong format of user",
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, null);
        }
        User user = userService.verifyUser(newUser);
        if (user != null) {
            currentUser = user;
            return ResponseHandler.responseBuilder(
                    "Log in as " + newUser.getLogin(), HttpStatus.OK, null);
        }
        return ResponseHandler.responseBuilder("Wrong password",
                HttpStatus.NOT_ACCEPTABLE, null);

    }

    /**
     * Method sets current user to null if it exist.
     *
     * @return Confirmation of success or error
     */
    @PostMapping("logout")
    public ResponseEntity<Object> logOut() {
        if (currentUser == null) {
            return ResponseHandler.responseBuilder(NOT_LOGGED_IN,
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
        currentUser = null;
        return ResponseHandler.responseBuilder("Logged out successfully",
                HttpStatus.OK, null);
    }

    //GET
    /**
     * Method calls userService to retrieve login and permissions of all users/
     *
     * @return Confirmation of success with List of Users data or error
     */
    @GetMapping("getUsers")
    public ResponseEntity<Object> getAllUsers() {
        if (currentUser == null) {
            return ResponseHandler.responseBuilder(NOT_LOGGED_IN,
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
        if (!currentUser.getPermission()) {
            return ResponseHandler.responseBuilder(
                    NOT_ENOGUHT_PERMISSION, HttpStatus.UNAUTHORIZED, null);
        }
        return ResponseHandler.responseBuilder(CALL_REACHED,
                HttpStatus.OK, userService.getUsers());
    }

    //UPDATE
    /**
     * Method calls userService to change permission of user to admin's.
     *
     * @param login String login of User's profile to change it's permissions.
     * @return Confirmation of success or error
     */
    @PutMapping("promote")
    public ResponseEntity<Object> promoteUser(@RequestParam(name = "login") String login) {
        if (currentUser == null) {
            return ResponseHandler.responseBuilder("You are not logged in",
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
        if (!currentUser.getPermission()) {
            return ResponseHandler.responseBuilder(
                    NOT_ENOGUHT_PERMISSION, HttpStatus.UNAUTHORIZED, null);
        }
        return ResponseHandler.responseBuilder(CALL_REACHED, HttpStatus.OK,
                userService.promoteUser(login));
    }

    //DELETE
    /**
     * Method calls userSerivce to delete user by it's login.
     *
     * @param login String user's login to delete
     * @return Confirmation of success or error
     */
    @DeleteMapping("delete")
    public ResponseEntity<Object> deleteUser(@RequestParam(name = "login") String login) {
        if (currentUser == null) {
            return ResponseHandler.responseBuilder(NOT_LOGGED_IN,
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
        if (!currentUser.getPermission()) {
            return ResponseHandler.responseBuilder(
                    NOT_ENOGUHT_PERMISSION, HttpStatus.UNAUTHORIZED, null);
        }
        return ResponseHandler.responseBuilder(CALL_REACHED, HttpStatus.OK,
                userService.deleteUser(login));
    }

    //////////////////////////GAME CALLS////////////////////////////////////////
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
        if (currentUser == null) {
            return ResponseHandler.responseBuilder(NOT_LOGGED_IN,
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
        if (!currentUser.getPermission()) {
            return ResponseHandler.responseBuilder(
                    NOT_ENOGUHT_PERMISSION, HttpStatus.UNAUTHORIZED, null);
        }
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
    public ResponseEntity<Object> getGameById(@PathVariable String stringId
    ) {

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
    public ResponseEntity<Object> getGameByName(@PathVariable String name
    ) {
        List<Game> list = gameService.findGameByName(name);
        if (list.isEmpty()) {
            return ResponseHandler.responseBuilder(EMPTY_COLLECTION,
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
            return ResponseHandler.responseBuilder(EMPTY_COLLECTION,
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
    public ResponseEntity<Object> getSpecificGames(@PathVariable String variable
    ) {
        List<Game> list;
        try {
            int price = Integer.parseInt(variable);
            list = gameService.showStockLessThan(price);
            if (list.isEmpty()) {
                return ResponseHandler.responseBuilder(EMPTY_COLLECTION,
                        HttpStatus.BAD_REQUEST, null);
            }
            return ResponseHandler.responseBuilder(CALL_REACHED,
                    HttpStatus.OK, list);
        } catch (NumberFormatException e) {
            try {
                Game.Genre genre = Game.Genre.valueOf(variable);
                list = gameService.getGameByGenre(genre);
                if (list.isEmpty()) {
                    return ResponseHandler.responseBuilder(EMPTY_COLLECTION,
                            HttpStatus.BAD_REQUEST, null);
                }
                return ResponseHandler.responseBuilder(CALL_REACHED,
                        HttpStatus.OK, list);
            } catch (IllegalArgumentException ee) {
                list = gameService.getGameByDev(variable);
                if (list.isEmpty()) {
                    return ResponseHandler.responseBuilder(EMPTY_COLLECTION,
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
    public ResponseEntity<Object> isAvaliable(@PathVariable String stringId
    ) {
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
    public ResponseEntity<Object> buyOneGame(@PathVariable String stringId
    ) {
        if (currentUser == null) {
            return ResponseHandler.responseBuilder(NOT_LOGGED_IN,
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
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
        if (currentUser == null) {
            return ResponseHandler.responseBuilder(NOT_LOGGED_IN,
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
        if (!currentUser.getPermission()) {
            return ResponseHandler.responseBuilder(
                    NOT_ENOGUHT_PERMISSION, HttpStatus.UNAUTHORIZED, null);
        }
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
        if (currentUser == null) {
            return ResponseHandler.responseBuilder(NOT_LOGGED_IN,
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
        if (!currentUser.getPermission()) {
            return ResponseHandler.responseBuilder(
                    NOT_ENOGUHT_PERMISSION, HttpStatus.UNAUTHORIZED, null);
        }
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
        if (currentUser == null) {
            return ResponseHandler.responseBuilder(NOT_LOGGED_IN,
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
        if (!currentUser.getPermission()) {
            return ResponseHandler.responseBuilder(
                    NOT_ENOGUHT_PERMISSION, HttpStatus.UNAUTHORIZED, null);
        }
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
        if (currentUser == null) {
            return ResponseHandler.responseBuilder(NOT_LOGGED_IN,
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
        if (!currentUser.getPermission()) {
            return ResponseHandler.responseBuilder(
                    NOT_ENOGUHT_PERMISSION, HttpStatus.UNAUTHORIZED, null);
        }
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
        if (currentUser == null) {
            return ResponseHandler.responseBuilder(NOT_LOGGED_IN,
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
        if (!currentUser.getPermission()) {
            return ResponseHandler.responseBuilder(
                    NOT_ENOGUHT_PERMISSION, HttpStatus.UNAUTHORIZED, null);
        }
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
        if (currentUser == null) {
            return ResponseHandler.responseBuilder(NOT_LOGGED_IN,
                    HttpStatus.NOT_ACCEPTABLE, null);
        }
        if (!currentUser.getPermission()) {
            return ResponseHandler.responseBuilder(
                    NOT_ENOGUHT_PERMISSION, HttpStatus.UNAUTHORIZED, null);
        }
        return ResponseHandler.responseBuilder(CALL_REACHED,
                HttpStatus.OK, gameService.deleteAllUnstockGames());
    }

}
