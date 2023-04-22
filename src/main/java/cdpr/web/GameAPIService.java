package cdpr.web;

import cdpr.web.resources.Game;
import cdpr.web.resources.Genre;
import java.util.HashMap;
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
    
    @GetMapping("{stringId}")
    public Game getGameById(@PathVariable String stringId) {
            try{
                int id = Integer.parseInt(stringId);
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
    @PutMapping("/admin")
    public String restockQuantity(@RequestParam(name="id") String stringId, @RequestParam(name="q") String stringQuantity){
        try{
            int id = Integer.parseInt(stringId);
            int quantity = Integer.parseInt(stringQuantity);
            if(quantity < 1){
                return "Quantity must be grater than 0 to restock";
            }
            if(games.containsKey(id)){
                quantity += games.get(id).getQuantity();
                games.get(id).setQuantity(quantity);
                return "Restock succesfully";
            }
            return "Such game does not exist";
        }catch(Exception e){
            System.out.println("Cannot parse String to int");
            return null;
        }
    }
    @PutMapping
    public String decreaseQuantity(@RequestParam(name="id") String stringId){
        try{
            int id = Integer.parseInt(stringId);

            if(games.containsKey(id)){
                int quantity = games.get(id).getQuantity();
                if(quantity > 0) {
                    games.get(id).setQuantity(quantity-1);
                    return "Quantity Decrased";
                }
                return "Product not in stock";
            }
            return "Such game does not exist";
        }catch(Exception e){
            System.out.println("Cannot parse String to int");
            return null;
        }
    }
    
    
    
    @DeleteMapping("admin/{stringId}")
    public String deleteGame(@PathVariable String stringId){
        try {
            int id = Integer.parseInt(stringId);
            if(games.containsKey(id)){
                games.remove(id);
                return "Game deleted";
            }
            return "Such game does not exist";
        }catch(Exception e){
            System.out.println("Cannot parse String to int");
            return null;
        }
    }
            
            
}
