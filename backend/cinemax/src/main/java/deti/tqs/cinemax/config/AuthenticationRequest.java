package deti.tqs.cinemax.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationRequest {
 
    private String username;
    private String password;
}