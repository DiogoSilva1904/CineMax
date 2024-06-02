package deti.tqs.cinemax.dataInit;

import deti.tqs.cinemax.models.*;
import deti.tqs.cinemax.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Profile("test")
public class DataInit implements CommandLineRunner {

    private MovieService movieService;

    private RoomService roomService;

    private SessionService sessionService;

    private UserService userService;

    private ReservationService reservationService;

    @Autowired
    public DataInit(MovieService movieService, RoomService roomService, SessionService sessionService, UserService userService, ReservationService reservationService) {
        this.movieService = movieService;
        this.roomService = roomService;
        this.sessionService = sessionService;
        this.userService = userService;
        this.reservationService = reservationService;
    }

    public void run(String... args) throws Exception {
        this.loadData();
    }

    private void loadData(){
        AppUser adminUser = new AppUser();
        adminUser.setId(1L);
        adminUser.setRole("ADMIN");
        adminUser.setPassword("admin");
        adminUser.setEmail("admin@gmail.com");
        adminUser.setUsername("admin");

        userService.saveAdminUser(adminUser);

        AppUser user1 = new AppUser();
        user1.setId(2L);
        user1.setRole("USER");
        user1.setPassword("user");
        user1.setEmail("jojo@gmail.com");
        user1.setUsername("user");

        userService.saveUser(user1);

        Movie movie1 = new Movie();
        movie1.setId(1L);
        movie1.setTitle("Inception");
        movie1.setCategory("Science Fiction");
        movie1.setGenre("Action");
        movie1.setStudio("Warner Bros.");
        movie1.setDuration("148");

        movieService.saveMovie(movie1);

        Movie movie2 = new Movie();
        movie2.setId(2L);

        movie2.setTitle("The Shawshank Redemption");
        movie2.setCategory("Drama");
        movie2.setGenre("Crime");
        movie2.setStudio("Castle Rock Entertainment");
        movie2.setDuration("142");

        movieService.saveMovie(movie2);

        Movie movie3 = new Movie();
        movie3.setId(3L);

        movie3.setTitle("The Godfather");
        movie3.setCategory("Crime");
        movie3.setGenre("Drama");
        movie3.setStudio("Paramount Pictures");
        movie3.setDuration("175");

        movieService.saveMovie(movie3);

        Room room1 = new Room();
        room1.setId(1L);
        room1.setName("Room A");
        room1.setCapacity(50);
        room1.setType("Standard");

        roomService.saveRoom(room1);

        Room room2 = new Room();
        room2.setId(2L);
        room2.setName("Room B");
        room2.setCapacity(30);
        room2.setType("Premium");

        roomService.saveRoom(room2);

        Room room3 = new Room();
        room3.setId(3L);
        room3.setName("Room C");
        room3.setCapacity(100);
        room3.setType("Standard");

        roomService.saveRoom(room3);

        Session session1 = new Session();
        session1.setId(1L);
        session1.setDate("2024-05-23");
        session1.setTime("20:00");
        session1.setMovie(movie1);
        session1.setRoom(room1);
        session1.setBookedSeats(List.of("A2", "A1"));

        sessionService.saveSession(session1);

        Session session2 = new Session();
        session2.setId(2L);

        session2.setDate("2024-05-16");
        session2.setTime("20:00");
        session2.setMovie(movie2);
        session2.setRoom(room2);
        session2.setBookedSeats(List.of("B3", "C5"));

        sessionService.saveSession(session2);

        Session session3 = new Session();
        session3.setId(3L);
        session3.setDate("2024-05-28");
        session3.setTime("22:00");
        session3.setMovie(movie1);
        session3.setRoom(room3);
        session3.setBookedSeats(List.of("C1", "C2"));

        sessionService.saveSession(session3);

        Reservation reservation1 = new Reservation();
        reservation1.setSession(session1);
        reservation1.setSeatNumbers(List.of("A3"));
        reservation1.setUser(user1);//need to be changed
        reservation1.setUsed(false);
        reservation1.setPrice(10);

        reservationService.saveReservation(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setSession(session2);
        reservation2.setSeatNumbers(List.of("B4","B6"));
        reservation2.setUsed(false);
        reservation2.setUser(user1);//need to be changed
        reservation2.setPrice(30);

        reservationService.saveReservation(reservation2);

        Reservation reservation3 = new Reservation();
        reservation3.setSession(session3);
        reservation3.setSeatNumbers(List.of("C3"));
        reservation3.setUser(adminUser);//need to be changed
        reservation3.setUsed(false);
        reservation3.setPrice(20);

        reservationService.saveReservation(reservation3);

        log.info("Data setup for IT complete.");
    }
}
