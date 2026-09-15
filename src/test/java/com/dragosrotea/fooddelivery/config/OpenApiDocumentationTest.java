package com.dragosrotea.fooddelivery.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OpenApiDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void exposesOpenApiDocumentWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("Food Delivery API"))
                .andExpect(jsonPath("$.info.version").value("1.0.0"))
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth").exists())
                .andExpect(jsonPath("$.paths['/api/orders']").exists())
                .andExpect(jsonPath("$.paths['/api/restaurants/{restaurantId}/availability']").exists())
                .andExpect(jsonPath("$.paths['/api/restaurants/{restaurantId}/menu-items/{menuItemId}']").exists())
                .andExpect(jsonPath("$.paths['/api/restaurants/{restaurantId}/menu-items/{menuItemId}/availability']").exists())
                .andExpect(jsonPath("$.paths['/api/admin/restaurants/{restaurantId}/menu-items']").exists());
    }
}
