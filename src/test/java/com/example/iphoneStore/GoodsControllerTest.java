package com.example.iphoneStore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.example.iphoneStore.handlers.GlobalExceptionHandler.ERROR_MESSAGE;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GoodsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String goodsJson = "{\"name\":\"testName\",\"price\":1.0,\"quantity\":20}";

    @Test
    void testUnauthorizedRequest() throws Exception {
        mockMvc.perform(post("/api/goods/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goodsJson)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    void testGoodsWasAddedByManager() throws Exception {
        mockMvc.perform(post("/api/goods/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goodsJson)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("testName"))
                .andExpect(jsonPath("$.price").value(1.0))
                .andExpect(jsonPath("$.quantity").value(20));
    }

    @Test
    @WithMockUser(username = "manager", authorities = {"CLIENT"})
    void testGoodsWasNotAddedByClient() throws Exception {
        mockMvc.perform(post("/api/goods/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goodsJson)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    void testGoodsWithSameNameWasNotAdded() throws Exception {
        mockMvc.perform(post("/api/goods/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goodsJson)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("testName"))
                .andExpect(jsonPath("$.price").value(1.0))
                .andExpect(jsonPath("$.quantity").value(20));

        mockMvc.perform(post("/api/goods/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goodsJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath(ERROR_MESSAGE).value("Goods with the same name already exist."));
    }

    @Test
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    void testGetAllGoods() throws Exception {
        final String goodsJson2 = "{\"name\":\"testName2\",\"price\":2.0,\"quantity\":30}";

        mockMvc.perform(post("/api/goods/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goodsJson)
                );

        mockMvc.perform(post("/api/goods/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goodsJson2)
                );

        mockMvc.perform(get("/api/goods/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", hasItems("testName", "testName2")))
                .andExpect(jsonPath("$[*].price", hasItems(1.0, 2.0)))
                .andExpect(jsonPath("$[*].quantity", hasItems(20, 30)));
    }

    @Test
    @WithMockUser(username = "client", authorities = {"CLIENT"})
    void testGetAllGoodsByClient() throws Exception {
        mockMvc.perform(get("/api/goods/")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    void testInvalidJson() throws Exception {
        mockMvc.perform(post("/api/goods/")
                        .content("Invalid Json")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ERROR_MESSAGE).value("Invalid JSON format"));
    }
}
