package com.develhope.testUnit;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testCreateUser() throws Exception {
        User user = new User();
        user.setName("Roronoa");
        user.setSurname("Zoro");


        Mockito.when(userService.createUser(Mockito.any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Roronoa\",\"surname\":\"Zoro\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Roronoa"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("Zoro"));
    }

    @Test
    public void testGetAllUsers() throws Exception {

        List<User> userList = new ArrayList<>();
        userList.add(new User(1L, "Roronoa", "Zoro"));
        userList.add(new User(2L, "Nico", "Robin"));

        Mockito.when(userService.getAllUsers()).thenReturn(userList);

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Roronoa"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname").value("Zoro"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Nico"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].surname").value("Robin"));
    }

    @Test
    public void testGetUserById() throws Exception {

        User user = new User(1L, "Roronoa", "Zoro");

        Mockito.when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("Doe"));
    }

    @Test
    public void testUpdateUser() throws Exception {

        Long userId = 1L;
        User updatedUser = new User();
        updatedUser.setName("Roronoa");
        updatedUser.setSurname("Zoro");

        User existingUser = new User(userId, "Roronoa", "Zoro");

        Mockito.when(userService.updateUser(Mockito.any(Long.class), Mockito.any(User.class))).thenReturn(updatedUser);
        Mockito.when(userService.getUserById(userId)).thenReturn(Optional.of(existingUser));

        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Roronoa\",\"surname\":\"Zoro\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Roronoa"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("Zoro"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        // Arrange
        Long userId = 1L;

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", userId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}