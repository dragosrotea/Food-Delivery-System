package com.dragosrotea.fooddelivery.restaurant.api;

import com.dragosrotea.fooddelivery.restaurant.MenuItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Admin Menu Items")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/admin/restaurants/{restaurantId}/menu-items")
public class AdminMenuItemController {

    private final MenuItemService menuItemService;

    public AdminMenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @Operation(summary = "List all available and unavailable menu items")
    @GetMapping
    public List<MenuItemResponse> getAllMenuItems(
            @PathVariable Long restaurantId
    ) {
        return menuItemService.getAllMenuItemsForAdmin(restaurantId).stream()
                .map(MenuItemResponse::from)
                .toList();
    }
}
