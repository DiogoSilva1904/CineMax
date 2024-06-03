package deti.tqs.cinemax.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.mindrot.jbcrypt.BCrypt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deti.tqs.cinemax.models.AppUser;
import deti.tqs.cinemax.repositories.UserRepository;
import deti.tqs.cinemax.config.CustomUserDetailsService;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final CustomUserDetailsService customUserDetailsService;

    private final UserRepository userRepository;

    @Autowired
    public UserService(CustomUserDetailsService customUserDetailsService, UserRepository userRepository) {
        this.customUserDetailsService = customUserDetailsService;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve.
     * @return the user with the specified ID, or null if not found.
     */
    public AppUser getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Saves a new user with the role "USER".
     *
     * @param user the user to save.
     * @return the saved user.
     */
    public AppUser saveUser(AppUser user) {
        UserDetails newUser = User.builder()
            .username(user.getUsername())
            .password(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()))
            .roles("USER")
            .build();
        
        customUserDetailsService.addUser(newUser);
        user.setRole("USER");
        return userRepository.save(user);
    }

    /**
     * Saves a new admin user with the role "ADMIN".
     *
     * @param user the admin user to save.
     * @return the saved admin user.
     */
    public AppUser saveAdminUser(AppUser user) {
        UserDetails newUser = User.builder()
            .username(user.getUsername())
            .password(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()))
            .roles("ADMIN")
            .build();
        
        customUserDetailsService.addUser(newUser);
        user.setRole("ADMIN");
        return userRepository.save(user);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete.
     */
    public void deleteUser(Long id) {
        AppUser user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return;
        }
        customUserDetailsService.deleteUser(user.getUsername());
        userRepository.deleteById(id);
    }

    /**
     * Updates an existing user.
     *
     * @param user the user with updated details.
     * @return the updated user.
     */
    public AppUser updateUser(AppUser user) {
        return userRepository.save(user);
    }

    /**
     * Retrieves all users from the repository.
     *
     * @return a list of all users.
     */
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user to retrieve.
     * @return the user with the specified username, or null if not found.
     */
    public AppUser getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Loads a user's details by their username.
     *
     * @param username the username of the user.
     * @return the UserDetails of the user.
     * @throws UsernameNotFoundException if the user with the specified username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customUserDetailsService.loadUserByUsername(username);
    }
}
