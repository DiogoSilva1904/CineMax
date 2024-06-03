package deti.tqs.cinemax.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import deti.tqs.cinemax.config.AuthenticationRequest;
import deti.tqs.cinemax.config.CustomUserDetailsService;
import deti.tqs.cinemax.config.IAuthenticationFacade;
import deti.tqs.cinemax.config.JwtUtilService;
import deti.tqs.cinemax.models.AppUser;
import deti.tqs.cinemax.services.FileService;
import deti.tqs.cinemax.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.security.crypto.bcrypt.BCrypt;

import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


@WebMvcTest(AuthController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtUtilService jwtUtil;

    @MockBean
    private IAuthenticationFacade authenticationFacade;

    @MockBean
    private FileService fileService;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCreateAuthenticationToken_ValidCredentials() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("admin", "admin");
        UserDetails userDetails = new org.springframework.security.core.userdetails.User("admin", "admin", Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        when(customUserDetailsService.loadUserByUsernameAndPassword("admin","admin")).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("token");

        MvcResult result = mvc.perform(post("/api/login").contentType("application/json").content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JsonObject jsonObject = new JsonParser().parse(content).getAsJsonObject();
        String jwt = jsonObject.get("jwt").getAsString();
        assertEquals("token", jwt);
    }

    @Test
    void testRegister_UserExists() throws Exception {
        String username = "existingUser";
        AppUser existingUser = new AppUser(1L, username, "password", "email", "role", Collections.emptyList());

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        mvc.perform(post("/api/login/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"" + username + "\", \"password\": \"password\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("User already exists"));

        verify(customUserDetailsService, times(1)).loadUserByUsername(username);
        verify(userService, never()).saveUser(existingUser);
    }

    @Test
    void testRegister_NewUser() throws Exception {
        String username = "newUser";
        AppUser newUser = new AppUser(2L, username, "password", "email", "role", Collections.emptyList());

        when(customUserDetailsService.loadUserByUsername(username)).thenThrow(new UsernameNotFoundException("User not found"));
        when(userService.saveUser(any(AppUser.class))).thenReturn(newUser);

        mvc.perform(post("/api/login/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"" + username + "\", \"password\": \"password\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully"));

        verify(customUserDetailsService, times(1)).loadUserByUsername(username);
        verify(userService, times(1)).saveUser(any(AppUser.class));
    }

    @Test
    void testChangePassword_Success() throws Exception {
        String username = "existingUser";
        String currentPassword = "oldPassword";
        String newPassword = "newPassword";

        AppUser user = new AppUser(1L, username, BCrypt.hashpw(currentPassword, BCrypt.gensalt()), "email", "role", Collections.emptyList());
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, BCrypt.hashpw(currentPassword, BCrypt.gensalt()), Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(userService.getUserByUsername(username)).thenReturn(user);
        when(userService.updateUser(user)).thenReturn(user);
        
        mvc.perform(put("/api/login/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"" + username + "\", \"currentPassword\": \"" + currentPassword + "\", \"newPassword\": \"" + newPassword + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Password changed successfully"));

        verify(customUserDetailsService, times(1)).loadUserByUsername(username);
        verify(customUserDetailsService, times(1)).updateUserPassword(username, newPassword);
        verify(userService, times(1)).getUserByUsername(username);
        verify(userService, times(1)).updateUser(user);
    }

}
