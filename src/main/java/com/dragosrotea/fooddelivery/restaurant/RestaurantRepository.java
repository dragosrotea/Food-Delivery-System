package com.dragosrotea.fooddelivery.restaurant;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    boolean existsByNameIgnoreCase(String name);
}
