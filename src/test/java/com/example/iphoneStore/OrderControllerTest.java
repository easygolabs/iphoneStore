package com.example.iphoneStore;

import com.example.iphoneStore.exceptions.GoodsNotFoundException;
import com.example.iphoneStore.exceptions.InsufficientQuantityException;
import com.example.iphoneStore.exceptions.OrderNotFoundException;
import com.example.iphoneStore.exceptions.UserNotFoundException;
import com.example.iphoneStore.model.Order;
import com.example.iphoneStore.repository.GoodsRepository;
import com.example.iphoneStore.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.example.iphoneStore.handlers.GlobalExceptionHandler.ERROR_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private OrderRepository orderRepository;

    private final String goodsJson = "{\"name\":\"testName\",\"price\":1.0,\"quantity\":5}";

    @Test
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    void testOrderPlaced() throws Exception {
        mockMvc.perform(post("/api/goods/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(goodsJson)
        );

        Long testGoodsId = goodsRepository.findByName("testName").get().getId();

        mockMvc.perform(post("/api/orders/")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJson(testGoodsId, 1L))
                )
                .andExpect(status().isCreated());
    }

    @Test
    void testUnauthorizedRequest() throws Exception {
        mockMvc.perform(post("/api/orders/")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"id\":100,\"quantity\":1}]")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "client", authorities = {"CLIENT"})
    void testGoodsNotFoundException() throws Exception {
        mockMvc.perform(post("/api/orders/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"id\":100,\"quantity\":1}]")
                        .param("userId", "1")
                )
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof GoodsNotFoundException))
                .andExpect(jsonPath(ERROR_MESSAGE).value("Could not find goods with id: 100"));
    }

    @Test
    @WithMockUser(username = "client", authorities = {"CLIENT"})
    void testUserNotFoundException() throws Exception {
        mockMvc.perform(post("/api/orders/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"id\":1,\"quantity\":1}]")
                        .param("userId", "200")
                )
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andExpect(jsonPath(ERROR_MESSAGE).value("User not found with id: 200"));
    }

    @Test
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    void testInsufficientQuantityException() throws Exception {
        mockMvc.perform(post("/api/goods/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(goodsJson)
        );

        Long testGoodsId = goodsRepository.findByName("testName").get().getId();

        mockMvc.perform(post("/api/orders/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJson(testGoodsId, 10L))
                        .param("userId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InsufficientQuantityException))
                .andExpect(jsonPath(ERROR_MESSAGE)
                        .value("Not enough quantity of goods with id: " + testGoodsId));
    }

    @Test
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    void testOrderAlreadyPaidException() throws Exception {
        Order testOrder = new Order();
        testOrder.setPaid(true);
        orderRepository.save(testOrder);

        Long paidOrderId = orderRepository.findByUser(null).get().getId();

        mockMvc.perform(put("http://localhost:8080/api/orders/" + paidOrderId + "/payment"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage")
                        .value("Order with id " + paidOrderId + " has already been paid for."));
    }

    @Test
    @WithMockUser(username = "client", authorities = {"CLIENT"})
    void testOrderPaidSuccessfully() throws Exception {
        Order testOrder = new Order();
        testOrder.setPaid(false);
        orderRepository.save(testOrder);

        Long paidOrderId = orderRepository.findByUser(null).get().getId();

        mockMvc.perform(put("http://localhost:8080/api/orders/" + paidOrderId + "/payment"))
                .andExpect(status().isOk());

        assertTrue(orderRepository.findByUser(null).get().isPaid());
    }

    @Test
    @WithMockUser(username = "client", authorities = {"CLIENT"})
    void testOrderNotFoundException() throws Exception {
        mockMvc.perform(put("http://localhost:8080/api/orders/500/payment"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof OrderNotFoundException))
                .andExpect(jsonPath(ERROR_MESSAGE).value("Not found. Order id: 500"));
    }

    private String getJson(Long testGoodsId, Long quantity) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Long> map = new HashMap<>();
        map.put("id", testGoodsId);
        map.put("quantity", quantity);

        return objectMapper.writeValueAsString(List.of(map));
    }
}
