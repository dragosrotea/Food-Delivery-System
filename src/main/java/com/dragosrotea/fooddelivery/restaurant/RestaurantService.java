package com.dragosrotea.fooddelivery.restaurant;

import com.dragosrotea.fooddelivery.restaurant.exception.DuplicateRestaurantException;
import com.dragosrotea.fooddelivery.restaurant.exception.RestaurantNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional(readOnly = true)
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findByActiveTrue();
    }

    @Transactional(readOnly = true)
    public List<Restaurant> getAllRestaurantsForAdmin() {
        return restaurantRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Restaurant getRestaurant(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
    }

    public Restaurant createRestaurant(String name, String street, String city) {
        if (restaurantRepository.existsByNameIgnoreCase(name)) {
            throw new DuplicateRestaurantException(name);
        }
        return restaurantRepository.save(new Restaurant(name, street, city));
    }

    public Restaurant changeAvailability(Long id, boolean active) {
        Restaurant restaurant = getRestaurant(id);
        restaurant.changeActiveStatus(active);
        return restaurant;
    }
}
