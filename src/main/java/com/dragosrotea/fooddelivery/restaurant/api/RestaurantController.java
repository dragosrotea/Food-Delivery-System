package com.dragosrotea.fooddelivery.restaurant.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import com.dragosrotea.fooddelivery.restaurant.Restaurant;
import com.dragosrotea.fooddelivery.restaurant.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Restaurants")
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Operation(summary = "List restaurants")
    @GetMapping
    public List<RestaurantResponse> getRestaurants() {
        return restaurantService.getAllRestaurants()
                .stream()
                .map(RestaurantResponse::from)
                .toList();
    }

    @Operation(summary = "Get a restaurant")
    @GetMapping("/{restaurantId}")
    public RestaurantResponse getRestaurant(@PathVariable Long restaurantId) {
        return RestaurantResponse.from(restaurantService.getRestaurant(restaurantId));
    }

    @Operation(summary = "Create a restaurant")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<RestaurantResponse> createRestaurant(
            @Valid @RequestBody CreateRestaurantRequest request
    ) {
        Restaurant restaurant = restaurantService.createRestaurant(
                request.name(),
                request.street(),
                request.city()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(RestaurantResponse.from(restaurant));
    }

    @Operation(summary = "Delete a restaurant")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long restaurantId) {
        restaurantService.deleteRestaurant(restaurantId);
        return ResponseEntity.noContent().build();
    }
}
