package deti.tqs.cinemax.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import deti.tqs.cinemax.repositories.SessionRepository;
import deti.tqs.cinemax.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import deti.tqs.cinemax.models.AppUser;
import deti.tqs.cinemax.models.Reservation;
import deti.tqs.cinemax.models.Session;
import deti.tqs.cinemax.repositories.ReservationRepository;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveReservation() {
        AppUser user = new AppUser();
        user.setUsername("user1");
        user.setPassword("password");
        user.setEmail("user1@example.com");
        user.setRole("USER");
        userRepository.save(user);

        Session session = new Session();
        session.setDate("2024-06-01");
        session.setTime("10:00");
        sessionRepository.save(session);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setPrice(10);
        reservation.setUsed(false);
        reservation.setSession(session);

        Reservation savedReservation = reservationRepository.save(reservation);

        assertThat(savedReservation.getId()).isNotNull();
    }

    @Test
    void testFindByUserUsername() {
        AppUser user = new AppUser();
        user.setUsername("user2");
        user.setPassword("password");
        user.setEmail("user2@example.com");
        user.setRole("USER");
        userRepository.save(user);

        Session session = new Session();
        session.setDate("2024-06-01");
        session.setTime("12:00");
        sessionRepository.save(session);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setPrice(10);
        reservation.setUsed(false);
        reservation.setSession(session);
        reservationRepository.save(reservation);

        List<Reservation> reservations = reservationRepository.findByUserUsername("user2");
        assertThat(reservations).hasSize(1);
    }

}
