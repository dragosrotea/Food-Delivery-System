package com.dragosrotea.fooddelivery.restaurant;

import com.dragosrotea.fooddelivery.restaurant.exception.DuplicateMenuItemException;
import com.dragosrotea.fooddelivery.restaurant.exception.InvalidMenuItemPriceException;
import com.dragosrotea.fooddelivery.restaurant.exception.MenuItemNotFoundInRestaurantException;
import com.dragosrotea.fooddelivery.restaurant.exception.RestaurantNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class MenuItemService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public MenuItemService(
            RestaurantRepository restaurantRepository,
            MenuItemRepository menuItemRepository
    ) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Transactional(readOnly = true)
    public List<MenuItem> getAvailableMenu(Long restaurantId) {
        requireActiveRestaurant(restaurantId);
        return menuItemRepository.findByRestaurantIdAndAvailableTrue(restaurantId);
    }

    @Transactional(readOnly = true)
    public List<MenuItem> getAllMenuItemsForAdmin(Long restaurantId) {
        requireRestaurant(restaurantId);
        return menuItemRepository.findByRestaurantId(restaurantId);
    }

    public MenuItem addMenuItem(
            Long restaurantId,
            String name,
            String description,
            BigDecimal price,
            String category
    ) {
        Restaurant restaurant = requireRestaurant(restaurantId);
        validatePrice(price);

        String normalizedName = name.trim();
        String normalizedDescription = normalizeOptional(description);
        String normalizedCategory = category.trim();

        if (menuItemRepository.existsByRestaurantIdAndNameIgnoreCase(
                restaurantId, normalizedName)) {
            throw new DuplicateMenuItemException(restaurantId, normalizedName);
        }

        MenuItem menuItem = new MenuItem(
                restaurant,
                normalizedName,
                normalizedDescription,
                price,
                normalizedCategory
        );
        return menuItemRepository.save(menuItem);
    }

    public MenuItem updateMenuItem(
            Long restaurantId,
            Long menuItemId,
            String name,
            String description,
            BigDecimal price,
            String category
    ) {
        requireRestaurant(restaurantId);
        validatePrice(price);

        String normalizedName = name.trim();
        String normalizedDescription = normalizeOptional(description);
        String normalizedCategory = category.trim();
        MenuItem item = requireMenuItem(restaurantId, menuItemId);

        if (menuItemRepository.existsByRestaurantIdAndNameIgnoreCaseAndIdNot(
                restaurantId, normalizedName, menuItemId)) {
            throw new DuplicateMenuItemException(restaurantId, normalizedName);
        }

        item.updateDetails(
                normalizedName,
                normalizedDescription,
                price,
                normalizedCategory
        );
        return item;
    }

    public MenuItem changeAvailability(Long restaurantId, Long menuItemId, boolean available) {
        requireRestaurant(restaurantId);
        MenuItem item = requireMenuItem(restaurantId, menuItemId);
        item.changeAvailability(available);
        return item;
    }

    private void validatePrice(BigDecimal price) {
        if (price == null || price.signum() < 0) {
            throw new InvalidMenuItemPriceException(price);
        }
    }

    private String normalizeOptional(String value) {
        return value == null ? null : value.trim();
    }

    private MenuItem requireMenuItem(Long restaurantId, Long menuItemId) {
        return menuItemRepository.findByIdAndRestaurantId(menuItemId, restaurantId)
                .orElseThrow(() ->
                        new MenuItemNotFoundInRestaurantException(menuItemId, restaurantId)
                );
    }

    private Restaurant requireActiveRestaurant(Long restaurantId) {
        return restaurantRepository.findByIdAndActiveTrue(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));
    }

    private Restaurant requireRestaurant(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));
    }
}
