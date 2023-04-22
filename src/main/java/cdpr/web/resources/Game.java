package cdpr.web.resources;

import java.util.Objects;


/**
 *
 * @author Jan Michalec
 */
public class Game {
    private String name;
    private String developer;
    private Genre genre;
    private double price;
    private int quantity;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Game{" + "name=" + name + ", developer=" + developer + ", genre=" + genre + ", price=" + price + ", quantity=" + quantity + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
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
