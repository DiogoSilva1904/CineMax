package deti.tqs.cinemax.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangePasswordRequest {

    private String username;
    private String currentPassword;
    private String newPassword;
    
}
