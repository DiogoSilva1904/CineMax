package deti.tqs.cinemax;
import deti.tqs.cinemax.services.*;
import deti.tqs.cinemax.models.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Component
public class SetupData implements org.springframework.boot.CommandLineRunner
{
    private  MovieService movieService;

    private RoomService roomService;

    private SessionService sessionService;

    private UserService userService;


    public void run(String... args)  throws Exception {
        setup();
    }

    public void setup() {

        final String ADMIN_USERNAME = "admin";
        AppUser user1 = new AppUser();
        user1.setId(1L);
        user1.setUsername(ADMIN_USERNAME);
        user1.setPassword(ADMIN_USERNAME);
        user1.setEmail(ADMIN_USERNAME);
        user1.setRole("ADMIN");

        saveUserIfNotExists(user1);

        Movie movie1 = new Movie();
        movie1.setId(1L);
        movie1.setTitle("Inception");
        movie1.setCategory("Science Fiction");
        movie1.setGenre("Action");
        movie1.setStudio("Warner Bros.");
        movie1.setDuration("148 minutes");

        saveMovieIfNotExists(movie1);

        Movie movie2 = new Movie();
        movie2.setId(2L);

        movie2.setTitle("The Shawshank Redemption");
        movie2.setCategory("Drama");
        movie2.setGenre("Crime");
        movie2.setStudio("Castle Rock Entertainment");
        movie2.setDuration("142 minutes");

        saveMovieIfNotExists(movie2);

        Movie movie3 = new Movie();
        movie3.setId(3L);

        movie3.setTitle("The Godfather");
        movie3.setCategory("Crime");
        movie3.setGenre("Drama");
        movie3.setStudio("Paramount Pictures");
        movie3.setDuration("175 minutes");

        saveMovieIfNotExists(movie3);

        log.info("Movie data setup complete.");


        Room room1 = new Room();
        room1.setId(1L);
        room1.setName("Room A");
        room1.setCapacity(50);
        room1.setType("Standard");

        saveRoomIfNotExists(room1);

        Room room2 = new Room();
        room2.setId(2L);

        room2.setName("Room B");
        room2.setCapacity(30);
        room2.setType("Premium");

        saveRoomIfNotExists(room2);

        Room room3 = new Room();
        room3.setId(1L);

        room3.setName("Room C");
        room3.setCapacity(100);
        room3.setType("Standard");

        saveRoomIfNotExists(room3);

        log.info("Room data setup complete.");

        Session session1 = new Session();
        session1.setId(1L);
        session1.setDate("2024-05-23");
        session1.setTime("20:00");
        session1.setMovie(movie1);
        session1.setRoom(room1);
        session1.setBookedSeats(List.of("A2", "A1"));

        saveSessionIfNotExists(session1);

        Session session2 = new Session();
        session2.setId(2L);

        session2.setDate("2024-05-16");
        session2.setTime("20:00");
        session2.setMovie(movie2);
        session2.setRoom(room2);
        session2.setBookedSeats(List.of("B3", "C5"));

        saveSessionIfNotExists(session2);

        Session session3 = new Session();
        session3.setId(3L);

        session3.setDate("2024-05-28");
        session3.setTime("22:00");
        session3.setMovie(movie1);
        session3.setRoom(room3);
        session3.setBookedSeats(List.of("C1", "C2"));

        saveSessionIfNotExists(session3);

        log.info("Session data setup complete.");

    }

    private void saveSessionIfNotExists(Session session) {
        Session existingSession = sessionService.getSessionById(session.getId());
        if (existingSession == null) {
            sessionService.saveSession(session);
            log.info("Session saved with id {}", session.getId());
        } else {
            log.info("Session already exists with id {}", session.getId());
        }
    }


    private void saveRoomIfNotExists(Room room) {
        Room existingRoom = roomService.getRoomById(room.getId());
        if (existingRoom == null) {
            roomService.saveRoom(room);
            log.info("Room saved: {}", room.getName());
        } else {
            log.info("Room already exists: {}", room.getName());
        }
    }
    private void saveMovieIfNotExists(Movie movie) {
        Movie existingMovie = movieService.getMovieById(movie.getId());
        if (existingMovie == null) {
            movieService.saveMovie(movie);
            log.info("Movie saved: {}", movie.getTitle());
        } else {
            log.info("Movie already exists: {}", movie.getTitle());
        }
    }

    private void saveUserIfNotExists(AppUser user) {
        AppUser existingUser = userService.getUserById(user.getId());
        if (existingUser == null) {
            userService.saveAdminUser(user);
            log.info("User saved: {}", user.getUsername());
        } else {
            log.info("User already exists: {}", user.getUsername());
        }
    }
}
