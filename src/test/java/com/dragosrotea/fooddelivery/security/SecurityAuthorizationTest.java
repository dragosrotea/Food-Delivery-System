package com.dragosrotea.fooddelivery.security;

import com.dragosrotea.fooddelivery.driver.DriverAccountService;
import com.dragosrotea.fooddelivery.order.OrderService;
import com.dragosrotea.fooddelivery.restaurant.MenuItemService;
import com.dragosrotea.fooddelivery.restaurant.Restaurant;
import com.dragosrotea.fooddelivery.restaurant.RestaurantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestaurantService restaurantService;

    @MockitoBean
    private MenuItemService menuItemService;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private DriverAccountService driverAccountService;

    @Test
    void allowsPublicRestaurantReadingWithoutToken() throws Exception {
        when(restaurantService.getAllRestaurants()).thenReturn(List.of());

        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk());

        verify(restaurantService).getAllRestaurants();
    }

    @Test
    void rejectsProtectedRequestWithoutToken() throws Exception {
        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRestaurantJson()))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));

        verifyNoInteractions(restaurantService);
    }

    @Test
    void forbidsCustomerFromCreatingRestaurant() throws Exception {
        mockMvc.perform(post("/api/restaurants")
                        .with(role("CUSTOMER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRestaurantJson()))
                .andExpect(status().isForbidden());

        verifyNoInteractions(restaurantService);
    }

    @Test
    void allowsAdminToCreateRestaurant() throws Exception {
        Restaurant restaurant =
                new Restaurant("Urban Pizza", "10 Main Street", "Bucharest");
        when(restaurantService.createRestaurant(
                anyString(), anyString(), anyString()
        )).thenReturn(restaurant);

        mockMvc.perform(post("/api/restaurants")
                        .with(role("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRestaurantJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Urban Pizza"));
    }

    @Test
    void protectsAdminMenuListing() throws Exception {
        mockMvc.perform(get("/api/admin/restaurants/1/menu-items"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(menuItemService);
    }

    @Test
    void allowsAdminToListEveryMenuItem() throws Exception {
        when(menuItemService.getAllMenuItemsForAdmin(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/admin/restaurants/1/menu-items")
                        .with(role("ADMIN")))
                .andExpect(status().isOk());

        verify(menuItemService).getAllMenuItemsForAdmin(1L);
    }

    @Test
    void preventsPublicDriverRegistration() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "driver@example.com",
                                  "password": "secure-password",
                                  "role": "DRIVER"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value("CUSTOMER"));
    }

    @Test
    void forbidsCustomerFromCreatingDriver() throws Exception {
        mockMvc.perform(post("/api/admin/drivers")
                        .with(role("CUSTOMER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validDriverJson()))
                .andExpect(status().isForbidden());

        verifyNoInteractions(driverAccountService);
    }

    @Test
    void protectsDriverEndpointsWithoutToken() throws Exception {
        mockMvc.perform(get("/api/driver/orders/available"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(orderService);
    }

    @Test
    void forbidsCustomerFromDriverEndpoints() throws Exception {
        mockMvc.perform(get("/api/driver/orders/available")
                        .with(role("CUSTOMER")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(orderService);
    }

    @Test
    void allowsDriverToViewAvailableDeliveries() throws Exception {
        when(orderService.getAvailableDeliveries()).thenReturn(List.of());

        mockMvc.perform(get("/api/driver/orders/available")
                        .with(role("DRIVER")))
                .andExpect(status().isOk());

        verify(orderService).getAvailableDeliveries();
    }

    @Test
    void rejectsOrderPlacementWithoutToken() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validOrderJson()))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(orderService);
    }

    @Test
    void forbidsAdminFromPlacingCustomerOrder() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .with(role("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validOrderJson()))
                .andExpect(status().isForbidden());

        verifyNoInteractions(orderService);
    }

    @Test
    void forbidsCustomerFromAdminOrderApi() throws Exception {
        mockMvc.perform(get("/api/admin/orders")
                        .with(role("CUSTOMER")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(orderService);
    }

    private org.springframework.test.web.servlet.request.RequestPostProcessor role(
            String role
    ) {
        return jwt().authorities(
                new SimpleGrantedAuthority("ROLE_" + role)
        );
    }

    private String validRestaurantJson() {
        return """
                {
                  "name": "Urban Pizza",
                  "street": "10 Main Street",
                  "city": "Bucharest"
                }
                """;
    }

    private String validDriverJson() {
        return """
                {
                  "email": "driver@example.com",
                  "password": "secure-password"
                }
                """;
    }

    private String validOrderJson() {
        return """
                {
                  "restaurantId": 1,
                  "deliveryStreet": "10 Main Street",
                  "deliveryCity": "Cluj-Napoca",
                  "items": [{"menuItemId": 1, "quantity": 2}]
                }
                """;
    }
}
