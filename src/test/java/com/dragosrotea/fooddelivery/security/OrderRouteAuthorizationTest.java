package com.dragosrotea.fooddelivery.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderRouteAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void preventsDriverFromUsingCustomerOrderHistory() throws Exception {
        mockMvc.perform(get("/api/orders")
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("ROLE_DRIVER")
                        )))
                .andExpect(status().isForbidden());
    }

    @Test
    void preventsAdminFromUsingCustomerOrderHistory() throws Exception {
        mockMvc.perform(get("/api/orders")
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("ROLE_ADMIN")
                        )))
                .andExpect(status().isForbidden());
    }

    @Test
    void preventsDriverFromUsingCustomerOrderDetails() throws Exception {
        mockMvc.perform(get("/api/orders/1")
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("ROLE_DRIVER")
                        )))
                .andExpect(status().isForbidden());
    }
}
