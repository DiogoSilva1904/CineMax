package deti.tqs.cinemax.services;
import deti.tqs.cinemax.repositories.*;
import deti.tqs.cinemax.models.AppUser;
import deti.tqs.cinemax.models.Reservation;
import deti.tqs.cinemax.models.Session;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private SessionService sessionService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void testSaveReservationSuccess() {
        AppUser user = new AppUser();
        user.setUsername("user1");
        user.setId(1L);

        Session session = new Session();
        session.setId(1L);


        List<String> bookedSeats = new ArrayList<>();
        session.setBookedSeats(bookedSeats);
        sessionService.saveSession(session);

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(user);
        reservation.setSession(session);

        List<String> seatNumbers = new ArrayList<>();
        seatNumbers.add("A1");
        reservation.setSeatNumbers(seatNumbers);

        when(sessionService.getSessionById(1L)).thenReturn(session);
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation savedReservation = reservationService.saveReservation(reservation);

        assertNotNull(savedReservation);
        assertEquals(reservation.getUser(), savedReservation.getUser());
        assertEquals(reservation.getSession(), savedReservation.getSession());
        assertEquals(seatNumbers.size(), savedReservation.getSeatNumbers().size());
        assertTrue(savedReservation.getSeatNumbers().contains("A1"));
    }

    @Test
    public void testSaveReservation_SeatAlreadyBooked() {
        // Setup data (pre-book one seat)
        Session session = new Session();
        session.setId(1L);
        session.setBookedSeats(Collections.singletonList("A1"));
        sessionService.updateSession(session.getId(), session);

        AppUser user = new AppUser();
        user.setUsername("user1");
        user.setId(1L);
        List<String> selectedSeats = Arrays.asList("A1", "B2");

        Reservation reservation = new Reservation();
        reservation.setSession(session);
        reservation.setUser(user);
        reservation.setSeatNumbers(selectedSeats);

        when(sessionService.getSessionById(1L)).thenReturn(session);

        Reservation savedReservation = reservationService.saveReservation(reservation);

        assertNull(savedReservation);
    }

    @Test
     void testSaveReservationFailure_SeatAlreadyBooked() {
        AppUser user = new AppUser();
        user.setUsername("user1");
        user.setId(1L);

        Session session = new Session();
        session.setId(1L);
        List<String> bookedSeats = new ArrayList<>();
        bookedSeats.add("A1");
        session.setBookedSeats(bookedSeats);
        sessionService.saveSession(session);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setId(1L);
        reservation.setSession(session);
        List<String> seatNumbers = new ArrayList<>();
        seatNumbers.add("A1");
        reservation.setSeatNumbers(seatNumbers);

        Reservation savedReservation = reservationService.saveReservation(reservation);

        assertNull(savedReservation);
    }

    @Test
    void testGetReservationByIdSuccess() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        Reservation retrievedReservation = reservationService.getReservationById(1L);

        assertNotNull(retrievedReservation);
        assertEquals(reservation.getId(), retrievedReservation.getId());
    }

    @Test
    void testGetReservationByIdFailure() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        Reservation retrievedReservation = reservationService.getReservationById(1L);

        assertNull(retrievedReservation);
    }

    @Test
    void testUpdateReservationSuccess() {
        AppUser user = new AppUser();
        user.setUsername("user1");
        user.setId(1L);

        Session session = new Session();
        session.setId(1L);
        List<String> bookedSeats = new ArrayList<>();
        session.setBookedSeats(bookedSeats);
        sessionService.saveSession(session);

        Reservation existingReservation = new Reservation();
        existingReservation.setId(1L);
        existingReservation.setUser(user);
        existingReservation.setSession(session);
        List<String> existingSeatNumbers = new ArrayList<>();
        existingSeatNumbers.add("A1");
        existingReservation.setSeatNumbers(existingSeatNumbers);

        Reservation updatedReservation = new Reservation();
        updatedReservation.setUser(user);
        updatedReservation.setSession(session);
        List<String> updatedSeatNumbers = new ArrayList<>();
        updatedSeatNumbers.add("A2");
        updatedReservation.setSeatNumbers(updatedSeatNumbers);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(existingReservation));
        when(reservationRepository.save(updatedReservation)).thenReturn(updatedReservation);

        Optional<Reservation> updatedOptionalReservation = reservationService.updateReservation(1L, updatedReservation);

        assertTrue(updatedOptionalReservation.isPresent());
        Reservation returnedReservation = updatedOptionalReservation.get();
        assertEquals(existingReservation.getId(), returnedReservation.getId());
        assertEquals(updatedReservation.getSession(), returnedReservation.getSession());
        assertEquals(1, returnedReservation.getSeatNumbers().size());
        assertTrue(returnedReservation.getSeatNumbers().contains("A2"));
    }

    @Test
    void testSaveReservationWithSeatAlreadyBooked() {
        AppUser user = new AppUser();
        user.setUsername("user1");
        user.setId(1L);

        Session session = new Session();
        session.setId(1L);
        List<String> bookedSeats = new ArrayList<>();
        bookedSeats.add("A1");
        session.setBookedSeats(bookedSeats);
        sessionService.saveSession(session);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setId(1L);
        reservation.setSession(session);
        List<String> seatNumbers = new ArrayList<>();
        seatNumbers.add("A1");
        reservation.setSeatNumbers(seatNumbers);

        Reservation savedReservation = reservationService.saveReservation(reservation);

        assertNull(savedReservation);
    }

    @Test
    void testSaveReservationWithSeatAvailable() {
        AppUser user = new AppUser();
        user.setUsername("user1");
        user.setId(1L);

        Session session = new Session();
        session.setId(1L);
        List<String> bookedSeats = new ArrayList<>();
        bookedSeats.add("A1");
        session.setBookedSeats(bookedSeats);
        sessionService.saveSession(session);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setId(1L);
        reservation.setSession(session);
        List<String> seatNumbers = new ArrayList<>();
        seatNumbers.add("A2");
        reservation.setSeatNumbers(seatNumbers);

        Reservation savedReservation = reservationService.saveReservation(reservation);

        assertNull(savedReservation);
    }

    @Test
    void testDeleteReservationSuccess() {

        Session session = new Session();
        session.setId(1L);
        session.setBookedSeats(new ArrayList<>());

        AppUser user = new AppUser();
        user.setUsername("testUser");
        user.setId(1L);
        user.setPassword("testPassword");
        user.setEmail("testEmail");
        user.setRole("USER");

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(user);
        List<String> seatNumbers = new ArrayList<>();
        seatNumbers.add("A2");
        reservation.setSeatNumbers(seatNumbers);
        reservation.setSession(session);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(sessionService.getSessionById(1L)).thenReturn(session);

        reservationService.deleteReservation(reservation.getId());

        verify(reservationRepository).deleteById(1L);
        verify(reservationRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetAllReservationsEmpty() {

        List<Reservation> Reservations = new ArrayList<>();

        when(reservationRepository.findAll()).thenReturn(Reservations);
        
        List<Reservation> allReservations = reservationService.getAllReservations();        

        assertNotNull(allReservations);
        assertEquals(0, allReservations.size());
    }


    @Test
    public void testGetAllReservationsWithReservations() {

        Session session = new Session();
        session.setId(1L);
        session.setBookedSeats(new ArrayList<>());

        AppUser user = new AppUser();
        user.setUsername("testUser");
        user.setId(1L);
        user.setPassword("testPassword");
        user.setEmail("testEmail");
        user.setRole("USER");

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(user);
        List<String> seatNumbers = new ArrayList<>();
        seatNumbers.add("A2");
        reservation.setSeatNumbers(seatNumbers);
        reservation.setSession(session);


        List<Reservation> Reservations = new ArrayList<>();
        Reservations.add(reservation);

        when(reservationRepository.findAll()).thenReturn(Reservations);
        
        List<Reservation> allReservations = reservationService.getAllReservations();        

        assertNotNull(allReservations);
        assertEquals(1, allReservations.size());
    }



}
