package cdpr.web;

import cdpr.web.resources.Game;
import cdpr.web.resources.Genre;
import java.util.HashMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Jan Michalec
 */
@RestController
@RequestMapping("/games")
public class GameAPIService {
    
    private HashMap<Integer,Game> games = new HashMap<>();
    private int id = 0;
    public GameAPIService() {
        Game newGame = new Game("Heroes of Might & Magic V", "Nival", Genre.STRATEGY, 100.0,5);
        games.put(id++, newGame);
        newGame = new Game("Resident Evil 4","CAPCOM",Genre.SIMULATION,200.0,2 );
        games.put(id++,newGame);
        newGame = new Game("The Great Ace Attorney Chronicles","CAPCOM",Genre.STRATEGY,120.0,0);
        games.put(id++,newGame);
    }
    
    @GetMapping("{StringId}")
    public Game getGameById(@PathVariable String StringId) {
            try{
                int id = Integer.parseInt(StringId);
                if(games.containsKey(id)){
                    return games.get(id);
                }
                return null;  
            }catch(Exception e) {
                System.out.println("Cannot parse String to Int");
                return null;
            }
    }
    @GetMapping("all")
    public HashMap<Integer,Game> getGames() {
        return games;
    }
    
    @PostMapping
    public String createGame(@RequestBody Game newGame){
        for(Game existingGame : games.values()){
            if(existingGame.equals(newGame)){
                return "Such game already exist in database";
            }
        }
        games.put(id++, newGame);
        return "Post Ok";
    }
    @PutMapping
    public String updateQuantity(@)
}
