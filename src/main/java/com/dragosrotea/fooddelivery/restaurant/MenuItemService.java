package com.dragosrotea.fooddelivery.restaurant;

import com.dragosrotea.fooddelivery.restaurant.exception.DuplicateMenuItemException;
import com.dragosrotea.fooddelivery.restaurant.exception.InvalidMenuItemPriceException;
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
        requireRestaurant(restaurantId);
        return menuItemRepository.findByRestaurantIdAndAvailableTrue(restaurantId);
    }

    public MenuItem addMenuItem(
            Long restaurantId,
            String name,
            String description,
            BigDecimal price,
            String category
    ) {
        Restaurant restaurant = requireRestaurant(restaurantId);

        if (price == null || price.signum() < 0) {
            throw new InvalidMenuItemPriceException(price);
        }

        if (menuItemRepository.existsByRestaurantIdAndNameIgnoreCase(restaurantId, name)) {
            throw new DuplicateMenuItemException(restaurantId, name);
        }

        MenuItem menuItem = new MenuItem(restaurant, name, description, price, category);
        return menuItemRepository.save(menuItem);
    }

    private Restaurant requireRestaurant(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));
    }
}
