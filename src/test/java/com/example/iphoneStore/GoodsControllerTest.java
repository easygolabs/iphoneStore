package com.example.iphoneStore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static com.example.iphoneStore.handlers.GlobalExceptionHandler.ERROR_MESSAGE;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GoodsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private static final String API_GOODS = "/api/goods/";

    @Test
    void testUnauthorizedRequest() throws Exception {
        performPostGoods()
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    void testGoodsAddedByManager() throws Exception {
        performPostGoods()
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("testName"))
                .andExpect(jsonPath("$.price").value(1.0))
                .andExpect(jsonPath("$.quantity").value(20));
    }

    @Test
    @WithMockUser(username = "manager", authorities = {"CLIENT"})
    void testAddGoodsForbiddenForClient() throws Exception {
        performPostGoods()
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    void testAddGoodsWithSameNameRejected() throws Exception {
        performPostGoods();
        performPostGoods()
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath(ERROR_MESSAGE).value("Goods with the same name already exist."));
    }

    @Test
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    void testGetAllGoods() throws Exception {
        final String goodsJson2 = "{\"name\":\"testName2\",\"price\":2.0,\"quantity\":30}";

        performPostGoods();

        mockMvc.perform(
                    post(API_GOODS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(goodsJson2)
                );

        mockMvc.perform(
                    get(API_GOODS)
                            .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", hasItems("testName", "testName2")))
                .andExpect(jsonPath("$[*].price", hasItems(1.0, 2.0)))
                .andExpect(jsonPath("$[*].quantity", hasItems(20, 30)));
    }

    @Test
    @WithMockUser(username = "client", authorities = {"CLIENT"})
    void testGetAllGoodsByClientIsOk() throws Exception {
        mockMvc.perform(
                    get(API_GOODS)
                            .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    void testJsonShouldNotContainNullFields() throws Exception {
        final String missingNameFieldJson = "{\"price\":1.0,\"quantity\":20}";

        mockMvc.perform(
                        post(API_GOODS)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(missingNameFieldJson)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("The name, price OR quantity field cannot be null!"));
    }

    @Test
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    void testInvalidJson() throws Exception {
        mockMvc.perform(
                    post(API_GOODS)
                            .content("Invalid Json")
                            .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ERROR_MESSAGE).value("Invalid JSON format"));
    }

    private ResultActions performPostGoods() throws Exception {
        final String goodsJson = "{\"name\":\"testName\",\"price\":1.0,\"quantity\":20}";

        return mockMvc.perform(
                post(API_GOODS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goodsJson)
        );
    }
}
