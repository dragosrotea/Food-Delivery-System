package model;

public class Restaurant {

    private int id;
    private final String name;
    private final String street;
    private final String city;

    public Restaurant(int id, String name, String street, String city) {
        this.id = id;
        this.name = name;
        this.street = street;
        this.city = city;
    }

    //Overloading Constructor
    public Restaurant(String name, String street, String city) {
        this.name = name;
        this.street = street;
        this.city = city;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getName() {return name;}
    public String getStreet() {return street;}
    public String getCity() {return city;}

    @Override
    public String toString() {
        return String.format("%s (%s, %s)", name, street, city);
    }
}
