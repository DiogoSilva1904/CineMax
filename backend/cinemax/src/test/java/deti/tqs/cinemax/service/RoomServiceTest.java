package deti.tqs.cinemax.service;
import deti.tqs.cinemax.models.room;
import deti.tqs.cinemax.repositories.*;
import deti.tqs.cinemax.services.*;

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

@SpringBootTest
@Slf4j
class RoomServiceTest {

    @Autowired
    private roomService roomService;

    @MockBean
    private roomRepository roomRepository;

    @Test
    void testGetAllRooms() {
        List<room> expectedRooms = new ArrayList<>();
        expectedRooms.add(new room(null,"Room 1", 50, 10, 5, "Lecture Hall", null, null));
        expectedRooms.add(new room(null,"Room 2", 30, 6, 5, "Meeting Room", null, null));

        Mockito.when(roomRepository.findAll()).thenReturn(expectedRooms);

        log.info("Mocking roomRepository.findAll() to return {} rooms", expectedRooms.size());

        List<room> actualRooms = roomService.getAllRooms();

        assertEquals(expectedRooms.size(), actualRooms.size());
        for (int i = 0; i < expectedRooms.size(); i++) {
            assertEquals(expectedRooms.get(i).getName(), actualRooms.get(i).getName());
            assertEquals(expectedRooms.get(i).getCapacity(), actualRooms.get(i).getCapacity());
        }
    }

    @Test
    void testGetRoomById_Found() {
        Long id = 1L;
        room expectedRoom = new room(null,"Test Room", 20, 5, 4, "Screening Room", null, null);

        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.of(expectedRoom));

        log.info("Calling roomService.getRoomById(id={})", id);

        room actualRoom = roomService.getRoomById(id);

        assertNotNull(actualRoom);
        assertEquals(expectedRoom.getId(), actualRoom.getId());
        assertEquals(expectedRoom.getName(), actualRoom.getName());
        log.info("Retrieved room with id {}", id);
    }

    @Test
    void testGetRoomById_NotFound() {
        Long id = 1L;

        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.empty());

        log.info("Calling roomService.getRoomById(id={})", id);

        room actualRoom = roomService.getRoomById(id);

        assertNull(actualRoom);
        log.info("Room with id {} not found", id);
    }

    @Test
    void testSaveRoom() {
        room newRoom = new room(null,"New Room", 40, 8, 5, "Classroom", null, null);

        Mockito.when(roomRepository.save(newRoom)).thenReturn(newRoom);

        log.info("Calling roomService.saveRoom(room={})", newRoom);

        room savedRoom = roomService.saveRoom(newRoom);

        assertNotNull(savedRoom);
        assertEquals(newRoom.getId(), savedRoom.getId());
        assertEquals(newRoom.getName(), savedRoom.getName());
        log.info("Saved room: {}", savedRoom);
    }

    @Test
    void testUpdateRoom_Found() {
        Long id = 2L;
        room existingRoom = new room(id,"Existing Room", 35, 7, 5, "Conference Room", null, null);
        room updatedRoom = new room(id,"Updated Room", 35, 7, 5, "Conference Room", null, null);

        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.of(existingRoom));
        Mockito.when(roomRepository.save(updatedRoom)).thenReturn(updatedRoom);

        log.info("Calling roomService.updateRoom(id={}, room={})", id, updatedRoom);

        Optional<room> updatedOptionalRoom = roomService.updateRoom(id, updatedRoom);

        assertTrue(updatedOptionalRoom.isPresent());
        room actualRoom = updatedOptionalRoom.get();

        assertEquals(id, actualRoom.getId());
        assertEquals(updatedRoom.getName(), actualRoom.getName());
        assertEquals(updatedRoom.getCapacity(), actualRoom.getCapacity());
        log.info("Updated room with id {}", id);
    }

    @Test
    public void testUpdateRoom_NotFound() {
        Long id = 3L;
        room updatedRoom = new room(id,"Updated Room", 35, 7, 5, "Conference Room", null, null);

        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.empty());

        log.info("Calling roomService.updateRoom(id={}, room={})", id, updatedRoom);

        Optional<room> updatedOptionalRoom = roomService.updateRoom(id, updatedRoom);

        assertTrue(updatedOptionalRoom.isEmpty());
        log.info("Room with id {} not found for update", id);
    }
}