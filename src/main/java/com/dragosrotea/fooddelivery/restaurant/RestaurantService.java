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
    public Restaurant getActiveRestaurant(Long id) {
        return restaurantRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Restaurant getRestaurant(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
    }

    public Restaurant createRestaurant(String name, String street, String city) {
        String normalizedName = name.trim();
        String normalizedStreet = street.trim();
        String normalizedCity = city.trim();

        if (restaurantRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new DuplicateRestaurantException(normalizedName);
        }

        return restaurantRepository.save(
                new Restaurant(normalizedName, normalizedStreet, normalizedCity)
        );
    }

    public Restaurant updateRestaurant(Long id, String name, String street, String city) {
        Restaurant restaurant = getRestaurant(id);
        String normalizedName = name.trim();
        String normalizedStreet = street.trim();
        String normalizedCity = city.trim();

        if (restaurantRepository.existsByNameIgnoreCaseAndIdNot(normalizedName, id)) {
            throw new DuplicateRestaurantException(normalizedName);
        }

        restaurant.updateDetails(normalizedName, normalizedStreet, normalizedCity);
        return restaurant;
    }

    public Restaurant changeAvailability(Long id, boolean active) {
        Restaurant restaurant = getRestaurant(id);
        restaurant.changeActiveStatus(active);
        return restaurant;
    }
}
