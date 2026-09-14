package com.dragosrotea.fooddelivery.restaurant;

import jakarta.persistence.*;

@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 120)
    private String name;
    @Column(nullable = false, length = 160)
    private String street;
    @Column(nullable = false, length = 100)
    private String city;
    @Column(nullable = false)
    private boolean active = true;

    protected Restaurant() {}

    public Restaurant(String name, String street, String city) {
        this.name = name;
        this.street = street;
        this.city = city;
    }

    public void changeActiveStatus(boolean active) { this.active = active; }
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public boolean isActive() { return active; }
}
