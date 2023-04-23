package cdpr.web.resources;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.Objects;
import jakarta.persistence.Table;

/**
 * Class stores all necessary data for game object.
 *
 * @author Jan Michalec
 */
@Entity
@Table(name = "game_database")
public class Game {

    /**
     * Genre enum.
     */
    public enum Genre {
        /**
         * Types of Genres; 8 to choose from.
         */
        SHOOTER, RPG, PLATFORM, STRATEGY, OPENWORLD, SIMULATION,
        RTS, PUZZLE;
    }

    /**
     * Id number of Game. Each game has unique one.
     */
    @Id
    private Integer id;

    /**
     * String name of game.
     */
    private String name;
    /**
     * String name of developer of game.
     */
    private String developer;
    /**
     * Enum Genre type of game.
     */
    private Genre genre;
    /**
     * Double price of game.
     */
    private double price;
    /**
     * int numbers of game in stock.
     */
    private int quantity;

    /**
     * 3 Constructors. One plain, 1 with and without quantity.
     */
    public Game() {
    }

    public Game(String name, String developer, Genre genre, double price) {
        this.name = name;
        this.developer = developer;
        this.genre = genre;
        this.price = price;
        this.quantity = 0;
    }

    public Game(String name, String developer, Genre genre, double price,
            int quantity) {
        this.name = name;
        this.developer = developer;
        this.genre = genre;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * Gets id of Game.
     *
     * @return Integer id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets id to new one.
     *
     * @param id Integer id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets name of game
     *
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of game to new one.
     *
     * @param name String new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets name of developer of game.
     *
     * @return String name of developer
     */
    public String getDeveloper() {
        return developer;
    }

    /**
     * Sets developer name to new one.
     *
     * @param developer String new developer one
     */
    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    /**
     * Gets Genre type
     *
     * @return enum Genre
     */
    public Genre getGenre() {
        return genre;
    }

    /**
     * Sets new Genre for game.
     *
     * @param genre enum Genre new Genre
     */
    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    /**
     * Gets price of game.
     *
     * @return double price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets new price of game.
     *
     * @param price double new price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Multiplies price by factor.
     *
     * @param factor double factor to change price.
     */
    public void setOnSale(double factor) {
        this.price *= factor;
    }

    /**
     * Gets quantity of games in stock.
     *
     * @return int quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets new quantity of game.
     *
     * @param quantity int new quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Decrease quantity number by 1.
     */
    public void decreaseQuantity() {
        this.quantity--;
    }

    /**
     * Adds number of games to stock.
     *
     * @param quantity int number to add
     */
    public void restockQuantity(int quantity) {
        this.quantity += quantity;
    }

    @Override
    public String toString() {
        return "Game{" + "id=" + id + ", name=" + name
                + ", developer=" + developer + ", genre=" + genre
                + ", price=" + price + ", quantity=" + quantity + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Game other = (Game) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.developer, other.developer)) {
            return false;
        }
        return this.genre == other.genre;
    }

}
