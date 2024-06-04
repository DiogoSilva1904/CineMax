package deti.tqs.cinemax.controllers;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deti.tqs.cinemax.config.CustomUserDetailsService;
import deti.tqs.cinemax.config.JwtUtilService;

import deti.tqs.cinemax.config.AuthenticationRequest;
import deti.tqs.cinemax.config.AuthenticationResponse;
import deti.tqs.cinemax.config.ChangePasswordRequest;

import deti.tqs.cinemax.models.AppUser;
import deti.tqs.cinemax.services.UserService;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/login")
public class AuthController {

    private final CustomUserDetailsService userDetailsService;
    private final JwtUtilService jwtUtil;
    private UserService userService;

    @Autowired
    public AuthController(CustomUserDetailsService userDetailsService, JwtUtilService jwtUtil, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    

    @PostMapping
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest){
        
        try {
            userDetailsService.loadUserByUsernameAndPassword(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsernameAndPassword(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        String username = userDetails.getUsername();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();


        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt, username, role));
    }

    @PutMapping("/change-password")
    public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            // Load user details to get the current password
            UserDetails userDetails = userDetailsService.loadUserByUsername(changePasswordRequest.getUsername());
            if (!BCrypt.checkpw(changePasswordRequest.getCurrentPassword(), userDetails.getPassword())) {
                return ResponseEntity.badRequest().body("Incorrect current password");
            }

            userDetailsService.updateUserPassword(changePasswordRequest.getUsername(), changePasswordRequest.getNewPassword());
            AppUser user = userService.getUserByUsername(changePasswordRequest.getUsername());
            user.setPassword(changePasswordRequest.getNewPassword());
            userService.updateUser(user);
            return ResponseEntity.ok("Password changed successfully");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body("User not found");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody AppUser user) {
        try {
            userDetailsService.loadUserByUsername(user.getUsername());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        } catch (UsernameNotFoundException e) {
            userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        }
    }

    
}
