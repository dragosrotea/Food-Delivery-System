package services;

import database.RestaurantDAO;
import database.MenuDAO;
import model.Restaurant;
import model.MenuItem;

import java.util.List;

public class RestaurantService {
    private final RestaurantDAO restaurantDAO = new RestaurantDAO();
    private final MenuDAO menuDAO = new MenuDAO();

    public List<Restaurant> getAllRestaurants() {
        return restaurantDAO.getAllRestaurants();
    }

    public List<MenuItem> getMenuForRestaurant(int restaurantId) {
        if (restaurantId <= 0) {
            return List.of();
        }
        return menuDAO.getItemsByRestaurant(restaurantId);
    }

    public boolean addRestaurant(String name, String street, String city) {
        if (name.isEmpty() || street.isEmpty() || city.isEmpty()) {
            return false;
        }

        if (restaurantDAO.existsByName(name)) {
            return false;
        }

        Restaurant r = new Restaurant(name, street, city);
        restaurantDAO.saveRestaurant(r);
        return true;
    }

    public boolean deleteRestaurant(int restaurantId) {
        return restaurantDAO.deleteRestaurantById(restaurantId);
    }
}