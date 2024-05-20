package deti.tqs.cinemax.services;
import deti.tqs.cinemax.repositories.*;
import deti.tqs.cinemax.models.Reservation;
import deti.tqs.cinemax.models.Session;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void testSaveReservationSuccess() {
        Session session = new Session();
        session.setId(1L);
        List<String> bookedSeats = new ArrayList<>();
        session.setBookedSeats(bookedSeats);
        sessionService.saveSession(session); // Assuming saveSession works (not tested here)

        Reservation reservation = new Reservation();
        reservation.setUsername("testUser");
        reservation.setSession(session);
        List<String> seatNumbers = new ArrayList<>();
        seatNumbers.add("A1");
        reservation.setSeatNumbers(seatNumbers);

        Mockito.when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation savedReservation = reservationService.saveReservation(reservation);

        assertNotNull(savedReservation);
        assertEquals(reservation.getUsername(), savedReservation.getUsername());
        assertEquals(reservation.getSession(), savedReservation.getSession());
        assertEquals(seatNumbers.size(), savedReservation.getSeatNumbers().size());
        assertTrue(savedReservation.getSeatNumbers().contains("A1"));
    }

    @Test
     void testSaveReservationFailure_SeatAlreadyBooked() {
        Session session = new Session();
        session.setId(1L);
        List<String> bookedSeats = new ArrayList<>();
        bookedSeats.add("A1");
        session.setBookedSeats(bookedSeats);
        sessionService.saveSession(session);

        Reservation reservation = new Reservation();
        reservation.setUsername("testUser");
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

        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        Reservation retrievedReservation = reservationService.getReservationById(1L);

        assertNotNull(retrievedReservation);
        assertEquals(reservation.getId(), retrievedReservation.getId());
    }

    @Test
    void testGetReservationByIdFailure() {
        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        Reservation retrievedReservation = reservationService.getReservationById(1L);

        assertNull(retrievedReservation);
    }

    @Test
    void testUpdateReservationSuccess() {
        Session session = new Session();
        session.setId(1L);
        List<String> bookedSeats = new ArrayList<>();
        session.setBookedSeats(bookedSeats);
        sessionService.saveSession(session);

        Reservation existingReservation = new Reservation();
        existingReservation.setId(1L);
        existingReservation.setUsername("testUser");
        existingReservation.setSession(session);
        List<String> existingSeatNumbers = new ArrayList<>();
        existingSeatNumbers.add("A1");
        existingReservation.setSeatNumbers(existingSeatNumbers);

        Reservation updatedReservation = new Reservation();
        updatedReservation.setUsername("updatedUser");
        updatedReservation.setSession(session);
        List<String> updatedSeatNumbers = new ArrayList<>();
        updatedSeatNumbers.add("A2");
        updatedReservation.setSeatNumbers(updatedSeatNumbers);

        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.of(existingReservation));
        Mockito.when(reservationRepository.save(updatedReservation)).thenReturn(updatedReservation);

        Optional<Reservation> updatedOptionalReservation = reservationService.updateReservation(1L, updatedReservation);

        assertTrue(updatedOptionalReservation.isPresent());
        Reservation returnedReservation = updatedOptionalReservation.get();
        assertEquals(updatedReservation.getUsername(), returnedReservation.getUsername());
        assertEquals(updatedReservation.getSession(), returnedReservation.getSession());
        assertEquals(1, returnedReservation.getSeatNumbers().size());
        assertTrue(returnedReservation.getSeatNumbers().contains("A2"));
    }

    @Test
    void testSaveReservationWithSeatAlreadyBooked() {
        Session session = new Session();
        session.setId(1L);
        List<String> bookedSeats = new ArrayList<>();
        bookedSeats.add("A1");
        session.setBookedSeats(bookedSeats);
        sessionService.saveSession(session);

        Reservation reservation = new Reservation();
        reservation.setUsername("testUser");
        reservation.setSession(session);
        List<String> seatNumbers = new ArrayList<>();
        seatNumbers.add("A1");
        reservation.setSeatNumbers(seatNumbers);

        Reservation savedReservation = reservationService.saveReservation(reservation);

        assertNull(savedReservation);
    }

    @Test
    void testSaveReservationWithSeatAvailable() {
        Session session = new Session();
        session.setId(1L);
        List<String> bookedSeats = new ArrayList<>();
        bookedSeats.add("A1");
        session.setBookedSeats(bookedSeats);
        sessionService.saveSession(session);

        Reservation reservation = new Reservation();
        reservation.setUsername("testUser");
        reservation.setSession(session);
        List<String> seatNumbers = new ArrayList<>();
        seatNumbers.add("A2");
        reservation.setSeatNumbers(seatNumbers);

        Reservation savedReservation = reservationService.saveReservation(reservation);

        assertNull(savedReservation);
    }

    @Test
    void testDeleteReservationSuccess() {
        Long reservationId = 1L;

        Session session = new Session();
        session.setId(1L);
        List<String> bookedSeats = new ArrayList<>();
        bookedSeats.add("A1");
        session.setBookedSeats(bookedSeats);
        session.setAvailableSeats(10);

        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        reservation.setSession(session);
        List<String> seatNumbers = new ArrayList<>();
        seatNumbers.add("A1");
        reservation.setSeatNumbers(seatNumbers);

        Mockito.when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        Mockito.when(sessionService.getSessionById(session.getId())).thenReturn(session);

        reservationService.deleteReservation(reservationId);

        Mockito.verify(reservationRepository, Mockito.times(1)).deleteById(reservationId);
        assertFalse(session.getBookedSeats().contains("A1"));
        assertEquals(11, session.getAvailableSeats());
    }

    @Test
    void testDeleteReservationWithNonExistentSession() {
        Long reservationId = 1L;

        Reservation reservation = new Reservation();
        reservation.setId(reservationId);

        Session session = new Session();
        session.setId(1L);
        reservation.setSession(session);

        List<String> seatNumbers = new ArrayList<>();
        seatNumbers.add("A1");
        reservation.setSeatNumbers(seatNumbers);

        Mockito.when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        Mockito.when(sessionService.getSessionById(session.getId())).thenReturn(null);

        Exception exception = assertThrows(NullPointerException.class, () -> {
            reservationService.deleteReservation(reservationId);
        });

        String expectedMessage = "Cannot invoke \"deti.tqs.cinemax.models.Session.getBookedSeats()\" because \"session\" is null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        Mockito.verify(reservationRepository, Mockito.times(0)).deleteById(reservationId);
    }



}
