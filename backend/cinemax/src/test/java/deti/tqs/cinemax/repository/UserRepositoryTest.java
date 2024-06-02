package deti.tqs.cinemax.repository;

import static org.assertj.core.api.Assertions.assertThat;

import deti.tqs.cinemax.models.AppUser;
import deti.tqs.cinemax.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUsername() {
        AppUser user = new AppUser();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setRole("USER");
        userRepository.save(user);

        AppUser foundUser = userRepository.findByUsername("testUser");

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("testUser");
    }

    @Test
    void testFindUserByIdNotFound() {
        AppUser foundUser = userRepository.findById(100L).orElse(null);

        assertThat(foundUser).isNull();
    }

    @Test
    void testDeleteUserByIdSuccess() {
        AppUser user = new AppUser();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setRole("USER");
        userRepository.save(user);

        userRepository.deleteById(user.getId());

        Optional<AppUser> deletedUser = userRepository.findById(user.getId());
        assertThat(deletedUser).isEmpty();
    }

    @Test
    void testUpdateExistingUser() {
        AppUser user = new AppUser();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setRole("USER");
        userRepository.save(user);

        user.setEmail("newemail@example.com");

        AppUser updatedUser = userRepository.save(user);

        assertThat(updatedUser.getEmail()).isEqualTo("newemail@example.com");
    }



    @Test
    void testDeleteUserByIdNotFound() {
        userRepository.deleteById(100L);

        AppUser deletedUser = userRepository.findById(100L).orElse(null);
        assertThat(deletedUser).isNull();
    }

    @Test
    void testSaveExistingUser() {
        AppUser user = new AppUser();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setRole("USER");
        userRepository.save(user);

        AppUser updatedUser = userRepository.save(user);

        assertThat(updatedUser).isEqualTo(user);
    }

}
