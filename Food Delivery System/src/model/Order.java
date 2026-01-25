package model;

import java.util.ArrayList;
import java.util.List;

public class Order {

    public enum OrderStatus {
        PENDING, ARRIVING, DELIVERED
    }

    private int id;
    private final int customerID;
    private final int restaurantID;
    private Integer driverID;
    private OrderStatus status;
    private final List<MenuItem> items = new ArrayList<>();

    public Order(int id, int customerID, int restaurantID, Integer driverID, String statusStr) {
        this.id = id;
        this.customerID = customerID;
        this.restaurantID = restaurantID;
        this.driverID = driverID;
        this.status = parseStatus(statusStr);
    }

    public Order(int customerID, int restaurantID) {
        this.customerID = customerID;
        this.restaurantID = restaurantID;
        this.status = OrderStatus.PENDING;
    }

    private OrderStatus parseStatus(String statusStr) {
        try {
            return OrderStatus.valueOf(statusStr.toUpperCase());
        } catch (Exception e) {
            return OrderStatus.PENDING;
        }
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerID() { return customerID; }
    public int getRestaurantID() { return restaurantID; }

    public Integer getDriverID() { return driverID; }
    public void setDriverID(Integer driverID) { this.driverID = driverID; }

    public List<MenuItem> getItems() { return items; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public double getTotalPrice() {
        double price = 0;
        for (MenuItem item : items) {
            price += item.getPrice();
        }
        return price;
    }

    @Override
    public String toString() {
        return String.format("Order #%d | Status: %s", id, status);
    }
}
