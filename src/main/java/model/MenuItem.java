package model;

public class MenuItem {
    private int id;
    private final int restaurantId;
    private final String name;
    private final double price;
    private final String category;

    public MenuItem(int id, int restaurantId, String name, double price, String category) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public MenuItem(int restaurantId, String name, double price, String category) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public int getRestaurantId() {return restaurantId;}
    public String getName() {return name;}
    public double getPrice() {return price;}
    public String getCategory() {return category;}

    @Override
    public String toString() {
        return String.format("[%s] %s - %.2f RON", category, name, price);
    }
}
