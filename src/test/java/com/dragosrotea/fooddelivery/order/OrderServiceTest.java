package com.dragosrotea.fooddelivery.order;

import com.dragosrotea.fooddelivery.order.exception.InvalidOrderQuantityException;
import com.dragosrotea.fooddelivery.order.exception.InvalidOrderStatusTransitionException;
import com.dragosrotea.fooddelivery.order.exception.MenuItemRestaurantMismatchException;
import com.dragosrotea.fooddelivery.order.exception.OrderAccessDeniedException;
import com.dragosrotea.fooddelivery.restaurant.MenuItem;
import com.dragosrotea.fooddelivery.restaurant.MenuItemRepository;
import com.dragosrotea.fooddelivery.restaurant.Restaurant;
import com.dragosrotea.fooddelivery.restaurant.RestaurantRepository;
import com.dragosrotea.fooddelivery.user.UserAccount;
import com.dragosrotea.fooddelivery.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private UserRepository userRepository;
    @Mock private RestaurantRepository restaurantRepository;
    @Mock private MenuItemRepository menuItemRepository;
    @Mock private UserAccount customer;
    @Mock private Restaurant restaurant;
    @Mock private MenuItem menuItem;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(
                orderRepository,
                userRepository,
                restaurantRepository,
                menuItemRepository
        );
    }

    @Test
    void placesOrderWithServerCalculatedTotalAndPriceSnapshot() {
        when(userRepository.findByEmail("customer@example.com")).thenReturn(Optional.of(customer));
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.findById(10L)).thenReturn(Optional.of(menuItem));
        when(restaurant.getId()).thenReturn(1L);
        when(restaurant.isActive()).thenReturn(true);
        when(menuItem.getRestaurant()).thenReturn(restaurant);
        when(menuItem.isAvailable()).thenReturn(true);
        when(menuItem.getName()).thenReturn("Pizza");
        when(menuItem.getPrice()).thenReturn(new BigDecimal("32.50"));
        when(orderRepository.save(any(FoodOrder.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        FoodOrder result = orderService.placeOrder(
                "customer@example.com",
                1L,
                "10 Main Street",
                "Cluj-Napoca",
                List.of(new OrderLineCommand(10L, 2))
        );

        assertEquals(OrderStatus.PLACED, result.getStatus());
        assertEquals(new BigDecimal("65.00"), result.getTotalPrice());
        assertEquals("Pizza", result.getItems().get(0).getItemName());
        assertEquals(new BigDecimal("32.50"), result.getItems().get(0).getUnitPrice());
        verify(orderRepository).save(result);
    }

    @Test
    void rejectsQuantityThatIsNotPositive() {
        when(userRepository.findByEmail("customer@example.com")).thenReturn(Optional.of(customer));
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(restaurant.isActive()).thenReturn(true);

        assertThrows(InvalidOrderQuantityException.class, () -> orderService.placeOrder(
                "customer@example.com",
                1L,
                "10 Main Street",
                "Cluj-Napoca",
                List.of(new OrderLineCommand(10L, 0))
        ));
    }

    @Test
    void rejectsMenuItemFromAnotherRestaurant() {
        Restaurant anotherRestaurant = org.mockito.Mockito.mock(Restaurant.class);
        when(userRepository.findByEmail("customer@example.com")).thenReturn(Optional.of(customer));
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(restaurant.isActive()).thenReturn(true);
        when(menuItemRepository.findById(10L)).thenReturn(Optional.of(menuItem));
        when(menuItem.getRestaurant()).thenReturn(anotherRestaurant);
        when(anotherRestaurant.getId()).thenReturn(2L);

        assertThrows(MenuItemRestaurantMismatchException.class, () -> orderService.placeOrder(
                "customer@example.com",
                1L,
                "10 Main Street",
                "Cluj-Napoca",
                List.of(new OrderLineCommand(10L, 1))
        ));
    }

    @Test
    void preventsCustomerFromReadingAnotherCustomersOrder() {
        UserAccount owner = org.mockito.Mockito.mock(UserAccount.class);
        FoodOrder order = org.mockito.Mockito.mock(FoodOrder.class);
        when(orderRepository.findById(5L)).thenReturn(Optional.of(order));
        when(order.getCustomer()).thenReturn(owner);
        when(owner.getEmail()).thenReturn("owner@example.com");
        when(order.getId()).thenReturn(5L);

        assertThrows(OrderAccessDeniedException.class,
                () -> orderService.getOrder("other@example.com", false, 5L));
    }

    @Test
    void allowsOnlyNextOrderStatus() {
        FoodOrder order = org.mockito.Mockito.mock(FoodOrder.class);
        when(orderRepository.findById(5L)).thenReturn(Optional.of(order));
        when(order.getStatus()).thenReturn(OrderStatus.PLACED);

        orderService.updateStatus(5L, OrderStatus.CONFIRMED);

        verify(order).changeStatus(OrderStatus.CONFIRMED);
    }

    @Test
    void rejectsSkippedOrderStatus() {
        FoodOrder order = org.mockito.Mockito.mock(FoodOrder.class);
        when(orderRepository.findById(5L)).thenReturn(Optional.of(order));
        when(order.getStatus()).thenReturn(OrderStatus.PLACED);

        assertThrows(InvalidOrderStatusTransitionException.class,
                () -> orderService.updateStatus(5L, OrderStatus.DELIVERED));
    }
}
