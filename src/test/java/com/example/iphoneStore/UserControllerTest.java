package com.example.iphoneStore;

import com.example.iphoneStore.emums.Role;
import com.example.iphoneStore.model.User;
import com.example.iphoneStore.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.iphoneStore.handlers.GlobalExceptionHandler.ERROR_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final String testManager = "managerTest";

    @Test
    void shouldRegisterUser_WhenJsonIsValid() throws Exception {
        String userJson = "{ \"username\": \"" + testManager + "\", \"password\": \"password123\", \"role\": \"MANAGER\" }";

        mockMvc.perform(post("/registration")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(testManager))
                .andExpect(jsonPath("$.role").value("MANAGER"));

        User user = userRepository.findByUsername(testManager).get();
        userRepository.delete(user);
    }

    @Test
    void shouldThrowDataIntegrityViolationException_whenUsernameAlreadyExists() throws Exception {
        String userJson = "{ \"username\": \"" + testManager + "\", \"password\": \"password123\", \"role\": \"MANAGER\" }";

        User existingUser = new User();
        existingUser.setUsername(testManager);
        existingUser.setPassword("password123");
        existingUser.setRole(Role.MANAGER);

        userRepository.save(existingUser);

        mockMvc.perform(post("/registration")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof DataIntegrityViolationException))
                .andExpect(jsonPath(ERROR_MESSAGE).value("Username already exists."));

        userRepository.delete(existingUser);
    }

    @Test
    void shouldThrowIllegalArgumentException_whenRoleIsInvalid() throws Exception {
        String userJson = "{ \"username\": \"" + testManager + "\", \"password\": \"password123\", \"role\": \"INVALID_ROLE\" }";

        mockMvc.perform(post("/registration")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath(ERROR_MESSAGE).value("Invalid role provided"));
    }

    @Test
    void shouldThrowHttpMessageNotReadableException_whenJsonIsInvalid() throws Exception {
        mockMvc.perform(post("/registration")
                        .content("Invalid Json")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ERROR_MESSAGE).value("Invalid JSON format"));
    }
}
