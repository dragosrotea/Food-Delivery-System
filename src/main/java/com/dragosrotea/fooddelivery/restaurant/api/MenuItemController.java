package com.dragosrotea.fooddelivery.restaurant.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import com.dragosrotea.fooddelivery.restaurant.MenuItem;
import com.dragosrotea.fooddelivery.restaurant.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Menu Items")
@RestController
@RequestMapping("/api/restaurants/{restaurantId}/menu-items")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @Operation(summary = "Get a restaurant's available menu")
    @GetMapping
    public List<MenuItemResponse> getAvailableMenu(@PathVariable Long restaurantId) {
        return menuItemService.getAvailableMenu(restaurantId)
                .stream()
                .map(MenuItemResponse::from)
                .toList();
    }

    @Operation(summary = "Add a menu item")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<MenuItemResponse> addMenuItem(
            @PathVariable Long restaurantId,
            @Valid @RequestBody CreateMenuItemRequest request
    ) {
        MenuItem menuItem = menuItemService.addMenuItem(
                restaurantId,
                request.name(),
                request.description(),
                request.price(),
                request.category()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(MenuItemResponse.from(menuItem));
    }
}
