package com.dragosrotea.fooddelivery.restaurant;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByRestaurantId(Long restaurantId);

    List<MenuItem> findByRestaurantIdAndAvailableTrue(Long restaurantId);

    Optional<MenuItem> findByIdAndRestaurantId(Long id, Long restaurantId);

    boolean existsByRestaurantIdAndNameIgnoreCaseAndIdNot(Long restaurantId, String name, Long id);

    boolean existsByRestaurantIdAndNameIgnoreCase(Long restaurantId, String name);
}
