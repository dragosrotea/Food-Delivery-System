package com.dragosrotea.fooddelivery.restaurant.api;

import com.dragosrotea.fooddelivery.restaurant.Restaurant;
import com.dragosrotea.fooddelivery.restaurant.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @Operation(summary = "List active restaurants")
    @GetMapping
    public List<RestaurantResponse> getRestaurants() {
        return restaurantService.getAllRestaurants().stream()
                .map(RestaurantResponse::from)
                .toList();
    }

    @Operation(summary = "Get an active restaurant")
    @GetMapping("/{restaurantId}")
    public RestaurantResponse getRestaurant(@PathVariable Long restaurantId) {
        return RestaurantResponse.from(
                restaurantService.getActiveRestaurant(restaurantId)
        );
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

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestaurantResponse.from(restaurant));
    }

    @Operation(summary = "Update restaurant details")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{restaurantId}")
    public RestaurantResponse updateRestaurant(
            @PathVariable Long restaurantId,
            @Valid @RequestBody UpdateRestaurantRequest request
    ) {
        return RestaurantResponse.from(restaurantService.updateRestaurant(
                restaurantId,
                request.name(),
                request.street(),
                request.city()
        ));
    }

    @Operation(summary = "Activate or deactivate a restaurant")
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/{restaurantId}/availability")
    public RestaurantResponse changeAvailability(
            @PathVariable Long restaurantId,
            @Valid @RequestBody UpdateRestaurantAvailabilityRequest request
    ) {
        return RestaurantResponse.from(
                restaurantService.changeAvailability(restaurantId, request.active())
        );
    }
}
