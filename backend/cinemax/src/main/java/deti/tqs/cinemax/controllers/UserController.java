package deti.tqs.cinemax.controllers;


import deti.tqs.cinemax.models.AppUser;
import deti.tqs.cinemax.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "localhost:4200")
@RequestMapping("/api/users")
@Tag(name = "users", description = "Endpoints to manage users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable Long id) {
        AppUser user = userService.getUserById(id);
        if(user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Delete user by usernmae")
    @DeleteMapping("{username}")
    public ResponseEntity<AppUser> deleteUserByUsername(@PathVariable String username) {
        AppUser user = userService.getUserByUsername(username);
        if(user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.deleteUser(user.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
