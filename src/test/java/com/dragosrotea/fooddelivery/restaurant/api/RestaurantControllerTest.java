package com.dragosrotea.fooddelivery.restaurant.api;

import com.dragosrotea.fooddelivery.restaurant.Restaurant;
import com.dragosrotea.fooddelivery.restaurant.RestaurantService;
import com.dragosrotea.fooddelivery.restaurant.exception.DuplicateRestaurantException;
import com.dragosrotea.fooddelivery.restaurant.exception.RestaurantNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RestaurantControllerTest {

    @Mock
    private RestaurantService restaurantService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new RestaurantController(restaurantService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void returnsRestaurantsAsJson() throws Exception {
        Restaurant restaurant = new Restaurant("Urban Pizza", "10 Main Street", "Bucharest");
        when(restaurantService.getAllRestaurants()).thenReturn(List.of(restaurant));

        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Urban Pizza"))
                .andExpect(jsonPath("$[0].city").value("Bucharest"));
    }

    @Test
    void returnsNotFoundErrorForMissingRestaurant() throws Exception {
        when(restaurantService.getRestaurant(99L))
                .thenThrow(new RestaurantNotFoundException(99L));

        mockMvc.perform(get("/api/restaurants/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Restaurant with id 99 was not found"))
                .andExpect(jsonPath("$.path").value("/api/restaurants/99"));
    }

    @Test
    void createsRestaurantFromValidJson() throws Exception {
        Restaurant restaurant = new Restaurant("Urban Pizza", "10 Main Street", "Bucharest");
        when(restaurantService.createRestaurant(
                "Urban Pizza",
                "10 Main Street",
                "Bucharest"
        )).thenReturn(restaurant);

        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Urban Pizza",
                                  "street": "10 Main Street",
                                  "city": "Bucharest"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Urban Pizza"))
                .andExpect(jsonPath("$.street").value("10 Main Street"));

        verify(restaurantService).createRestaurant(
                "Urban Pizza",
                "10 Main Street",
                "Bucharest"
        );
    }

    @Test
    void rejectsInvalidRestaurantJson() throws Exception {
        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": " ",
                                  "street": "",
                                  "city": "Bucharest"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.fieldErrors.name").value("Name is required"))
                .andExpect(jsonPath("$.fieldErrors.street").value("Street is required"));

        verifyNoInteractions(restaurantService);
    }

    @Test
    void returnsConflictForDuplicateRestaurant() throws Exception {
        when(restaurantService.createRestaurant(
                anyString(),
                anyString(),
                anyString()
        )).thenThrow(new DuplicateRestaurantException("Urban Pizza"));

        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Urban Pizza",
                                  "street": "10 Main Street",
                                  "city": "Bucharest"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message")
                        .value("A restaurant named 'Urban Pizza' already exists"));
    }

    @Test
    void deletesRestaurantWithoutResponseBody() throws Exception {
        mockMvc.perform(delete("/api/restaurants/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(restaurantService).deleteRestaurant(1L);
    }
}
