package deti.tqs.cinemax.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationResponse {
    
    private final String jwt;
    private final String username;
    private final String role;
}
