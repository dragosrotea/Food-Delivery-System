package com.dragosrotea.fooddelivery.security;

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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestaurantService restaurantService;

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
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("ROLE_CUSTOMER")
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRestaurantJson()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));

        verifyNoInteractions(restaurantService);
    }

    @Test
    void allowsAdminToCreateRestaurant() throws Exception {
        Restaurant restaurant = new Restaurant(
                "Urban Pizza",
                "10 Main Street",
                "Bucharest"
        );
        when(restaurantService.createRestaurant(
                anyString(),
                anyString(),
                anyString()
        )).thenReturn(restaurant);

        mockMvc.perform(post("/api/restaurants")
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("ROLE_ADMIN")
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRestaurantJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Urban Pizza"));

        verify(restaurantService).createRestaurant(
                "Urban Pizza",
                "10 Main Street",
                "Bucharest"
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
}
