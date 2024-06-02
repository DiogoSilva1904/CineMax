package deti.tqs.cinemax.services;

import deti.tqs.cinemax.config.CustomUserDetailsService;
import deti.tqs.cinemax.models.AppUser;
import deti.tqs.cinemax.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@Slf4j
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetUserById_Found() {
        AppUser user = new AppUser();
        user.setId(1L);
        user.setRole("USER");
        user.setPassword("password");
        user.setEmail("dio@gmail.com");

        log.info("Mocking userRepository.findById({}) to return a user", user.getId());

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        AppUser retrievedUser = userService.getUserById(user.getId());

        assertEquals(user.getId(), retrievedUser.getId());
        assertEquals(user.getRole(), retrievedUser.getRole());
        assertEquals(user.getPassword(), retrievedUser.getPassword());
        assertEquals(user.getEmail(), retrievedUser.getEmail());
    }

    @Test
    void testSaveUser(){
        AppUser user = new AppUser();
        user.setId(1L);
        user.setRole("USER");
        user.setUsername("dio");
        user.setPassword("password");
        user.setEmail("dio@gmail.com");

        Mockito.when(userRepository.save(user)).thenReturn(user);

        AppUser savedUser = userService.saveUser(user);

        assertEquals(user.getId(), savedUser.getId());
        assertEquals(user.getRole(), savedUser.getRole());
        assertEquals(user.getPassword(), savedUser.getPassword());
        assertEquals(user.getEmail(), savedUser.getEmail());
    }

    @Test
    void testSaveAdminUser(){
        AppUser user = new AppUser();
        user.setId(1L);
        user.setUsername("admin");
        user.setRole("ADMIN");
        user.setPassword("admin");
        user.setEmail("admin@gmail.com");

        Mockito.when(userRepository.save(user)).thenReturn(user);

        AppUser savedUser = userService.saveAdminUser(user);

        assertEquals(user.getId(), savedUser.getId());
        assertEquals(user.getRole(), savedUser.getRole());
        assertEquals(user.getPassword(), savedUser.getPassword());
        assertEquals(user.getEmail(), savedUser.getEmail());
    }

    @Test
    void testDeleteUser(){
        AppUser user = new AppUser();
        user.setId(1L);
        user.setUsername("admin");
        user.setRole("ADMIN");
        user.setPassword("admin");
        user.setEmail("dio@gmail.com");

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.deleteUser(user.getId());

        Mockito.verify(customUserDetailsService, Mockito.times(1)).deleteUser(user.getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(user.getId());
    }

    @Test
    void testGetUserById_NotFound() {
        long userId = 1L;

        log.info("Mocking userRepository.findById({}) to return empty", userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        AppUser user = userService.getUserById(userId);

        assertNull(user);
        log.info("User with id {} not found", userId);
    }

    @Test
    void testGetUserByUsername_Found() {
        AppUser user = new AppUser();
        user.setId(1L);
        user.setRole("USER");
        user.setPassword("password");
        user.setEmail("idk");
        user.setUsername("dio");

        log.info("Mocking userRepository.findByUsername({}) to return a user", user.getUsername());
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        AppUser retrievedUser = userService.getUserByUsername(user.getUsername());

        assertEquals(user.getId(), retrievedUser.getId());
        assertEquals(user.getRole(), retrievedUser.getRole());
        assertEquals(user.getPassword(), retrievedUser.getPassword());
        assertEquals(user.getEmail(), retrievedUser.getEmail());
        assertEquals(user.getUsername(), retrievedUser.getUsername());
    }

    @Test
    void testGetUserByUsername_NotFound() {
        String username = "dio";

        log.info("Mocking userRepository.findByUsername({}) to return null", username);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(null);

        AppUser user = userService.getUserByUsername(username);

        assertNull(user);
        log.info("User with username {} not found", username);
    }

    @Test
    void testUpdateUser() {
        AppUser user = new AppUser();
        user.setId(1L);
        user.setRole("USER");
        user.setUsername("dio");
        user.setPassword("password");
        user.setEmail("2ofosjodfjo");

        Mockito.when(userRepository.save(user)).thenReturn(user);

        AppUser updatedUser = userService.updateUser(user);

        assertEquals(user.getId(), updatedUser.getId());
        assertEquals(user.getRole(), updatedUser.getRole());
        assertEquals(user.getPassword(), updatedUser.getPassword());
        assertEquals(user.getEmail(), updatedUser.getEmail());
    }

    @Test
    void testGetAllUsers() {
        AppUser user1 = new AppUser();
        user1.setId(1L);
        user1.setRole("USER");
        user1.setUsername("dio");
        user1.setPassword("password");
        user1.setEmail("ifk");

        AppUser user2 = new AppUser();
        user2.setId(2L);
        user2.setRole("USER");
        user2.setUsername("dio2");
        user2.setPassword("password");
        user2.setEmail("ioioio");

        Mockito.when(userRepository.findAll()).thenReturn(java.util.List.of(user1, user2));

        log.info("Mocking userRepository.findAll() to return a list of users");

        assertEquals(java.util.List.of(user1, user2), userService.getAllUsers());

    }

}
