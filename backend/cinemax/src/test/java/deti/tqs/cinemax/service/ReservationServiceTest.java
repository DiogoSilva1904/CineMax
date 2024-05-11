package deti.tqs.cinemax.service;
import deti.tqs.cinemax.models.session;
import deti.tqs.cinemax.repositories.*;
import deti.tqs.cinemax.services.*;
import deti.tqs.cinemax.models.reservation;
import deti.tqs.cinemax.models.session;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservationServiceTest {

    @Autowired
    private reservationService reservationService;

    @Autowired
    private sessionService sessionService;

    @MockBean
    private reservationRepository reservationRepository;

    @MockBean
    private sessionRepository sessionRepository;

    @Test
    void testSaveReservationSuccess() {
        session session = new session();
        session.setId(1L);
        List<String> bookedSeats = new ArrayList<>();
        session.setBookedSeats(bookedSeats);
        sessionService.saveSession(session); // Assuming saveSession works (not tested here)

        reservation reservation = new reservation();
        reservation.setUsername("testUser");
        reservation.setSession(session);
        List<String> seatNumbers = new ArrayList<>();
        seatNumbers.add("A1");
        reservation.setSeatNumbers(seatNumbers);

        Mockito.when(reservationRepository.save(reservation)).thenReturn(reservation);

        reservation savedReservation = reservationService.saveReservation(reservation);

        assertNotNull(savedReservation);
        assertEquals(reservation.getUsername(), savedReservation.getUsername());
        assertEquals(reservation.getSession(), savedReservation.getSession());
        assertEquals(seatNumbers.size(), savedReservation.getSeatNumbers().size());
        assertTrue(savedReservation.getSeatNumbers().contains("A1"));
    }

    @Test
     void testSaveReservationFailure_SeatAlreadyBooked() {
        session session = new session();
        session.setId(1L);
        List<String> bookedSeats = new ArrayList<>();
        bookedSeats.add("A1");
        session.setBookedSeats(bookedSeats);
        sessionService.saveSession(session);

        reservation reservation = new reservation();
        reservation.setUsername("testUser");
        reservation.setSession(session);
        List<String> seatNumbers = new ArrayList<>();
        seatNumbers.add("A1");
        reservation.setSeatNumbers(seatNumbers);

        reservation savedReservation = reservationService.saveReservation(reservation);

        assertNull(savedReservation);
    }

    @Test
    void testGetReservationByIdSuccess() {
        reservation reservation = new reservation();
        reservation.setId(1L);

        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        reservation retrievedReservation = reservationService.getReservationById(1L);

        assertNotNull(retrievedReservation);
        assertEquals(reservation.getId(), retrievedReservation.getId());
    }

    @Test
    void testGetReservationByIdFailure() {
        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        reservation retrievedReservation = reservationService.getReservationById(1L);

        assertNull(retrievedReservation);
    }

    @Test
    void testUpdateReservationSuccess() {
        session session = new session();
        session.setId(1L);
        List<String> bookedSeats = new ArrayList<>();
        session.setBookedSeats(bookedSeats);
        sessionService.saveSession(session);

        reservation existingReservation = new reservation();
        existingReservation.setId(1L);
        existingReservation.setUsername("testUser");
        existingReservation.setSession(session);
        List<String> existingSeatNumbers = new ArrayList<>();
        existingSeatNumbers.add("A1");
        existingReservation.setSeatNumbers(existingSeatNumbers);

        reservation updatedReservation = new reservation();
        updatedReservation.setUsername("updatedUser");
        updatedReservation.setSession(session);
        List<String> updatedSeatNumbers = new ArrayList<>();
        updatedSeatNumbers.add("A2");
        updatedReservation.setSeatNumbers(updatedSeatNumbers);

        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.of(existingReservation));
        Mockito.when(reservationRepository.save(updatedReservation)).thenReturn(updatedReservation);

        Optional<reservation> updatedOptionalReservation = reservationService.updateReservation(1L, updatedReservation);

        assertTrue(updatedOptionalReservation.isPresent());
        reservation returnedReservation = updatedOptionalReservation.get();
        assertEquals(updatedReservation.getUsername(), returnedReservation.getUsername());
        assertEquals(updatedReservation.getSession(), returnedReservation.getSession());
        assertEquals(1, returnedReservation.getSeatNumbers().size());
        assertTrue(returnedReservation.getSeatNumbers().contains("A2"));
    }



}
