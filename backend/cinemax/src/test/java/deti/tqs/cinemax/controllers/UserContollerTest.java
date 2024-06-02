package deti.tqs.cinemax.controllers;

import deti.tqs.cinemax.config.CustomUserDetailsService;
import deti.tqs.cinemax.config.IAuthenticationFacade;
import deti.tqs.cinemax.config.JwtUtilService;
import deti.tqs.cinemax.models.AppUser;
import deti.tqs.cinemax.models.Movie;
import deti.tqs.cinemax.models.Room;
import deti.tqs.cinemax.models.Session;
import deti.tqs.cinemax.services.RoomService;
import deti.tqs.cinemax.services.SessionService;
import deti.tqs.cinemax.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserContollerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtUtilService jwtUtil;

    @MockBean
    private IAuthenticationFacade authenticationFacade;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetUserByID() throws Exception{
        AppUser user = new AppUser();
        user.setId(1L);
        user.setRole("ADMIN");
        user.setPassword("admin");
        user.setEmail("admin@gmail.com");
        user.setUsername("admin");

        Mockito.when(userService.getUserById(1L)).thenReturn(user);

        mvc.perform(get("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.role", is("ADMIN")))
                .andExpect(jsonPath("$.password", is("admin")))
                .andExpect(jsonPath("$.email", is("admin@gmail.com")))
                .andExpect(jsonPath("$.username", is("admin")));
    }

    @Test
    void testDeleteUser() throws Exception{

        AppUser user = new AppUser();
        user.setId(1L);
        user.setRole("ADMIN");
        user.setPassword("admin");
        user.setEmail("admin@gmail.com");
        user.setUsername("admin");

        Mockito.when(userService.getUserByUsername("admin")).thenReturn(user);

        mvc.perform(delete("/api/users/admin")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void testDeleteUserNotFound() throws Exception{

        Mockito.when(userService.getUserByUsername("admin")).thenReturn(null);

        mvc.perform(delete("/api/users/admin")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }
}
