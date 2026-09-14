package com.dragosrotea.fooddelivery.order;

import com.dragosrotea.fooddelivery.order.exception.EmptyOrderException;
import com.dragosrotea.fooddelivery.order.exception.InvalidOrderQuantityException;
import com.dragosrotea.fooddelivery.order.exception.InvalidOrderStatusTransitionException;
import com.dragosrotea.fooddelivery.order.exception.MenuItemNotFoundException;
import com.dragosrotea.fooddelivery.order.exception.MenuItemRestaurantMismatchException;
import com.dragosrotea.fooddelivery.order.exception.MenuItemUnavailableException;
import com.dragosrotea.fooddelivery.order.exception.OrderAccessDeniedException;
import com.dragosrotea.fooddelivery.order.exception.OrderNotFoundException;
import com.dragosrotea.fooddelivery.restaurant.MenuItem;
import com.dragosrotea.fooddelivery.restaurant.MenuItemRepository;
import com.dragosrotea.fooddelivery.restaurant.Restaurant;
import com.dragosrotea.fooddelivery.restaurant.RestaurantRepository;
import com.dragosrotea.fooddelivery.restaurant.exception.RestaurantNotFoundException;
import com.dragosrotea.fooddelivery.restaurant.exception.RestaurantUnavailableException;
import com.dragosrotea.fooddelivery.user.UserAccount;
import com.dragosrotea.fooddelivery.user.UserRepository;
import com.dragosrotea.fooddelivery.user.UserRole;
import com.dragosrotea.fooddelivery.user.exception.InvalidCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private static final Map<OrderStatus, EnumSet<OrderStatus>> ALLOWED_TRANSITIONS = Map.of(
            OrderStatus.PLACED, EnumSet.of(OrderStatus.CONFIRMED, OrderStatus.CANCELLED),
            OrderStatus.CONFIRMED, EnumSet.of(OrderStatus.PREPARING, OrderStatus.CANCELLED),
            OrderStatus.PREPARING, EnumSet.of(OrderStatus.READY_FOR_PICKUP),
            OrderStatus.READY_FOR_PICKUP, EnumSet.of(OrderStatus.OUT_FOR_DELIVERY),
            OrderStatus.OUT_FOR_DELIVERY, EnumSet.of(OrderStatus.DELIVERED),
            OrderStatus.DELIVERED, EnumSet.noneOf(OrderStatus.class),
            OrderStatus.CANCELLED, EnumSet.noneOf(OrderStatus.class)
    );

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public OrderService(
            OrderRepository orderRepository,
            UserRepository userRepository,
            RestaurantRepository restaurantRepository,
            MenuItemRepository menuItemRepository
    ) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Transactional
    public FoodOrder placeOrder(
            String customerEmail,
            Long restaurantId,
            String deliveryStreet,
            String deliveryCity,
            List<OrderLineCommand> lines
    ) {
        if (lines == null || lines.isEmpty()) {
            throw new EmptyOrderException();
        }

        UserAccount customer = findUser(customerEmail);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));
        if (!restaurant.isActive()) {
            throw new RestaurantUnavailableException(restaurantId);
        }
        FoodOrder order = new FoodOrder(customer, restaurant, deliveryStreet.trim(), deliveryCity.trim());

        for (OrderLineCommand line : lines) {
            if (line.quantity() <= 0) {
                throw new InvalidOrderQuantityException();
            }

            MenuItem menuItem = menuItemRepository.findById(line.menuItemId())
                    .orElseThrow(() -> new MenuItemNotFoundException(line.menuItemId()));

            if (!menuItem.getRestaurant().getId().equals(restaurantId)) {
                throw new MenuItemRestaurantMismatchException(line.menuItemId(), restaurantId);
            }
            if (!menuItem.isAvailable()) {
                throw new MenuItemUnavailableException(line.menuItemId());
            }

            order.addItem(new OrderItem(order, menuItem, line.quantity()));
        }

        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<FoodOrder> getCustomerOrders(String customerEmail) {
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(findUser(customerEmail).getId());
    }

    @Transactional(readOnly = true)
    public FoodOrder getOrder(String requesterEmail, boolean admin, Long orderId) {
        FoodOrder order = findOrder(orderId);
        ensureOwnerOrAdmin(requesterEmail, admin, order);
        return order;
    }

    @Transactional(readOnly = true)
    public List<FoodOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public FoodOrder updateStatus(Long orderId, OrderStatus requestedStatus) {
        FoodOrder order = findOrder(orderId);
        if (!ALLOWED_TRANSITIONS.get(order.getStatus()).contains(requestedStatus)) {
            throw new InvalidOrderStatusTransitionException(order.getStatus(), requestedStatus);
        }
        order.changeStatus(requestedStatus);
        return order;
    }

    @Transactional
    public FoodOrder cancelOrder(String customerEmail, Long orderId) {
        FoodOrder order = findOrder(orderId);
        ensureOwnerOrAdmin(customerEmail, false, order);
        if (!ALLOWED_TRANSITIONS.get(order.getStatus()).contains(OrderStatus.CANCELLED)) {
            throw new InvalidOrderStatusTransitionException(order.getStatus(), OrderStatus.CANCELLED);
        }
        order.changeStatus(OrderStatus.CANCELLED);
        return order;
    }

    private UserAccount findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);
    }

    private FoodOrder findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    private void ensureOwnerOrAdmin(String requesterEmail, boolean admin, FoodOrder order) {
        if (!admin && !order.getCustomer().getEmail().equals(requesterEmail)) {
            throw new OrderAccessDeniedException(order.getId());
        }
    }
}
