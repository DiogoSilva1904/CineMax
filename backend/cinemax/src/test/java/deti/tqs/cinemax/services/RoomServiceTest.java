package deti.tqs.cinemax.services;
import deti.tqs.cinemax.models.Room;
import deti.tqs.cinemax.repositories.*;

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
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;


    @Test
    void testGetAllRooms() {
        List<Room> expectedRooms = new ArrayList<>();
        expectedRooms.add(new Room(null,"Room 1", 50, 10, 5, "Lecture Hall", null, null));
        expectedRooms.add(new Room(null,"Room 2", 30, 6, 5, "Meeting Room", null, null));

        Mockito.when(roomRepository.findAll()).thenReturn(expectedRooms);

        log.info("Mocking roomRepository.findAll() to return {} rooms", expectedRooms.size());

        List<Room> actualRooms = roomService.getAllRooms();

        assertEquals(expectedRooms.size(), actualRooms.size());
        for (int i = 0; i < expectedRooms.size(); i++) {
            assertEquals(expectedRooms.get(i).getName(), actualRooms.get(i).getName());
            assertEquals(expectedRooms.get(i).getCapacity(), actualRooms.get(i).getCapacity());
        }
    }

    @Test
    void testGetRoomById_Found() {
        Long id = 1L;
        Room expectedRoom = new Room(null,"Test Room", 20, 5, 4, "Screening Room", null, null);

        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.of(expectedRoom));

        log.info("Calling roomService.getRoomById(id={})", id);

        Room actualRoom = roomService.getRoomById(id);

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

        Room actualRoom = roomService.getRoomById(id);

        assertNull(actualRoom);
        log.info("Room with id {} not found", id);
    }

    @Test
    void testSaveRoom() {
        Room newRoom = new Room(null,"New Room", 40, 8, 5, "Classroom", null, null);

        Mockito.when(roomRepository.save(newRoom)).thenReturn(newRoom);

        log.info("Calling roomService.saveRoom(room={})", newRoom);

        Room savedRoom = roomService.saveRoom(newRoom);

        assertNotNull(savedRoom);
        assertEquals(newRoom.getId(), savedRoom.getId());
        assertEquals(newRoom.getName(), savedRoom.getName());
        log.info("Saved room: {}", savedRoom);
    }

    @Test
    void testUpdateRoom_Found() {
        Long id = 2L;
        Room existingRoom = new Room(id,"Existing Room", 35, 7, 5, "Conference Room", null, null);
        Room updatedRoom = new Room(id,"Updated Room", 35, 7, 5, "Conference Room", null, null);

        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.of(existingRoom));
        Mockito.when(roomRepository.save(updatedRoom)).thenReturn(updatedRoom);

        log.info("Calling roomService.updateRoom(id={}, room={})", id, updatedRoom);

        Optional<Room> updatedOptionalRoom = roomService.updateRoom(id, updatedRoom);

        assertTrue(updatedOptionalRoom.isPresent());
        Room actualRoom = updatedOptionalRoom.get();

        assertEquals(id, actualRoom.getId());
        assertEquals(updatedRoom.getName(), actualRoom.getName());
        assertEquals(updatedRoom.getCapacity(), actualRoom.getCapacity());
        log.info("Updated room with id {}", id);
    }

    @Test
    public void testUpdateRoom_NotFound() {
        Long id = 3L;
        Room updatedRoom = new Room(id,"Updated Room", 35, 7, 5, "Conference Room", null, null);

        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.empty());

        log.info("Calling roomService.updateRoom(id={}, room={})", id, updatedRoom);

        Optional<Room> updatedOptionalRoom = roomService.updateRoom(id, updatedRoom);

        assertTrue(updatedOptionalRoom.isEmpty());
        log.info("Room with id {} not found for update", id);
    }
}