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

    public AppUser getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public AppUser saveUser(AppUser user) {
        UserDetails newUser = User.builder()
            .username(user.getUsername())
            // bcrypt hash of a random password
            .password(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()))
            .roles("USER")
            .build();
        
        customUserDetailsService.addUser(newUser);
        user.setRole("USER");
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        AppUser user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return;
        }
        customUserDetailsService.deleteUser(user.getUsername());
        userRepository.deleteById(id);
    }

    public AppUser updateUser(AppUser user) {
        return userRepository.save(user);
    }

    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    public AppUser getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customUserDetailsService.loadUserByUsername(username);
    }
}
