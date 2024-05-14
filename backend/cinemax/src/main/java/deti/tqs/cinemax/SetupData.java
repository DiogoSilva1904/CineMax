package deti.tqs.cinemax;
import deti.tqs.cinemax.services.*;
import deti.tqs.cinemax.models.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class SetupData
{
    private  movieService movieService;

    private roomService roomService;

    private sessionService sessionService;

    public void setup() {

        movie movie1 = new movie();
        movie1.setId(1L);
        movie1.setTitle("Inception");
        movie1.setCategory("Science Fiction");
        movie1.setGenre("Action");
        movie1.setStudio("Warner Bros.");
        movie1.setDuration("148 minutes");

        saveMovieIfNotExists(movie1);

        movie movie2 = new movie();
        movie2.setId(2L);

        movie2.setTitle("The Shawshank Redemption");
        movie2.setCategory("Drama");
        movie2.setGenre("Crime");
        movie2.setStudio("Castle Rock Entertainment");
        movie2.setDuration("142 minutes");

        saveMovieIfNotExists(movie2);

        movie movie3 = new movie();
        movie3.setId(3L);

        movie3.setTitle("The Godfather");
        movie3.setCategory("Crime");
        movie3.setGenre("Drama");
        movie3.setStudio("Paramount Pictures");
        movie3.setDuration("175 minutes");

        saveMovieIfNotExists(movie3);

        log.info("Movie data setup complete.");


        room room1 = new room();
        room1.setId(1L);
        room1.setName("Room A");
        room1.setCapacity(50);
        room1.setNumberOfRows(5);
        room1.setColumns(10);
        room1.setType("Standard");

        saveRoomIfNotExists(room1);

        room room2 = new room();
        room2.setId(2L);

        room2.setName("Room B");
        room2.setCapacity(30);
        room2.setNumberOfRows(4);
        room2.setColumns(8);
        room2.setType("Premium");

        saveRoomIfNotExists(room2);

        room room3 = new room();
        room3.setId(1L);

        room3.setName("Room C");
        room3.setCapacity(100);
        room3.setNumberOfRows(8);
        room3.setColumns(12);
        room3.setType("Standard");

        saveRoomIfNotExists(room3);

        log.info("Room data setup complete.");

        session session1 = new session();
        session1.setId(1L);
        session1.setDate("2024-05-15");
        session1.setTime("18:00");
        session1.setMovie(movie1);
        session1.setRoom(room1);
        session1.setBookedSeats(List.of("A2", "A1"));

        saveSessionIfNotExists(session1);

        session session2 = new session();
        session2.setId(2L);

        session2.setDate("2024-05-16");
        session2.setTime("20:00");
        session2.setMovie(movie2);
        session2.setRoom(room2);
        session2.setBookedSeats(List.of("B3", "C5"));

        saveSessionIfNotExists(session2);

        log.info("Session data setup complete.");

    }

    private void saveSessionIfNotExists(session session) {
        session existingSession = sessionService.getSessionById(session.getId());
        if (existingSession == null) {
            sessionService.saveSession(session);
            log.info("Session saved with id {}", session.getId());
        } else {
            log.info("Session already exists with id {}", session.getId());
        }
    }


    private void saveRoomIfNotExists(room room) {
        room existingRoom = roomService.getRoomById(room.getId());
        if (existingRoom == null) {
            roomService.saveRoom(room);
            log.info("Room saved: {}", room.getName());
        } else {
            log.info("Room already exists: {}", room.getName());
        }
    }
    private void saveMovieIfNotExists(movie movie) {
        movie existingMovie = movieService.getMovieById(movie.getId());
        if (existingMovie == null) {
            movieService.saveMovie(movie);
            log.info("Movie saved: {}", movie.getTitle());
        } else {
            log.info("Movie already exists: {}", movie.getTitle());
        }
    }
}
