package com.dragosrotea.fooddelivery.restaurant.api;

import com.dragosrotea.fooddelivery.restaurant.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Admin Restaurants")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/admin/restaurants")
public class AdminRestaurantController {
    private final RestaurantService restaurantService;

    public AdminRestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Operation(summary = "List active and inactive restaurants")
    @GetMapping
    public List<RestaurantResponse> getAllRestaurants() {
        return restaurantService.getAllRestaurantsForAdmin().stream()
                .map(RestaurantResponse::from).toList();
    }
}
