package deti.tqs.cinemax.service;

import deti.tqs.cinemax.models.Seat;
import deti.tqs.cinemax.repositories.*;
import deti.tqs.cinemax.services.*;
import deti.tqs.cinemax.models.Reservation;
import deti.tqs.cinemax.models.Session;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@SpringBootTest
class SeatServiceTest {

    @Autowired
    private seatService seatService;

    @MockBean
    private seatRepository seatRepository;

    @Test
     void testGetAllSeats() {
        List<Seat> expectedSeats = new ArrayList<>();
        expectedSeats.add(new Seat(null, "A1", 1, null));
        expectedSeats.add(new Seat(null, "B2", 2, null));

        Mockito.when(seatRepository.findAll()).thenReturn(expectedSeats);

        log.info("Mocking seatRepository.findAll() to return {} seats", expectedSeats.size());

        List<Seat> actualSeats = seatService.getAllSeats();

        assertEquals(expectedSeats.size(), actualSeats.size());
        for (int i = 0; i < expectedSeats.size(); i++) {
            assertEquals(expectedSeats.get(i).getSeatIdentifier(), actualSeats.get(i).getSeatIdentifier());
            assertEquals(expectedSeats.get(i).getPriceMultiplier(), actualSeats.get(i).getPriceMultiplier());
        }
    }

    @Test
     void testGetSeatById_Found() {
        Long id = 1L;
        Seat expectedSeat = new Seat(id, "C3", 3, null);

        Mockito.when(seatRepository.findById(id)).thenReturn(Optional.of(expectedSeat));

        log.info("Calling seatService.getSeatById(id={})", id);

        Seat actualSeat = seatService.getSeatById(id);

        assertNotNull(actualSeat);
        assertEquals(expectedSeat.getId(), actualSeat.getId());
        assertEquals(expectedSeat.getSeatIdentifier(), actualSeat.getSeatIdentifier());
        assertEquals(expectedSeat.getPriceMultiplier(), actualSeat.getPriceMultiplier());
        log.info("Retrieved seat with id {}", id);
    }

    @Test
     void testGetSeatById_NotFound() {
        Long id = 1L;

        Mockito.when(seatRepository.findById(id)).thenReturn(Optional.empty());

        log.info("Calling seatService.getSeatById(id={})", id);

        Seat actualSeat = seatService.getSeatById(id);

        assertNull(actualSeat);
        log.info("Seat with id {} not found", id);
    }

    @Test
     void testSaveSeat() {
        Seat newSeat = new Seat(1L, "D4", 4, null);

        Mockito.when(seatRepository.save(newSeat)).thenReturn(newSeat);

        log.info("Calling seatService.saveSeat(seat={})", newSeat);

        Seat savedSeat = seatService.saveSeat(newSeat);

        assertNotNull(savedSeat);
        assertEquals(newSeat.getId(), savedSeat.getId());
        assertEquals(newSeat.getSeatIdentifier(), savedSeat.getSeatIdentifier());
        assertEquals(newSeat.getPriceMultiplier(), savedSeat.getPriceMultiplier());
        log.info("Saved seat: {}", savedSeat);
    }


    @Test
     void testUpdateSeat_Found() {
        Long id = 2L;
        Seat existingSeat = new Seat(id, "E5", 1, null);
        Seat updatedSeat = new Seat(id, "E5", 2, null);

        Mockito.when(seatRepository.findById(id)).thenReturn(Optional.of(existingSeat));
        Mockito.when(seatRepository.save(updatedSeat)).thenReturn(updatedSeat);

        log.info("Calling seatService.updateSeat(id={}, seat={})", id, updatedSeat);

        Optional<Seat> updatedOptionalSeat = seatService.updateSeat(id, updatedSeat);

        assertTrue(updatedOptionalSeat.isPresent());
        Seat actualSeat = updatedOptionalSeat.get();

        assertEquals(id, actualSeat.getId());
        assertEquals(updatedSeat.getSeatIdentifier(), actualSeat.getSeatIdentifier());
        assertEquals(updatedSeat.getPriceMultiplier(), actualSeat.getPriceMultiplier());
        log.info("Updated seat with id {}", id);
    }

}
