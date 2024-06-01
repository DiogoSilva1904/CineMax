package deti.tqs.cinemax.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import deti.tqs.cinemax.models.Room;
import deti.tqs.cinemax.repositories.RoomRepository;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Test
    void testSaveRoom() {
        Room room = new Room();
        room.setName("Sala 1");
        room.setCapacity(100);
        room.setType("Normal");

        Room savedRoom = roomRepository.save(room);

        assertThat(savedRoom.getId()).isNotNull();
    }

    @Test
    void testFindAllRooms() {
        Room room1 = new Room();
        room1.setName("Sala 1");
        room1.setCapacity(100);
        room1.setType("Normal");
        roomRepository.save(room1);

        Room room2 = new Room();
        room2.setName("Sala 2");
        room2.setCapacity(150);
        room2.setType("VIP");
        roomRepository.save(room2);

        List<Room> rooms = roomRepository.findAll();

        assertThat(rooms).hasSize(2);
    }

    @Test
    void testFindRoomById() {
        Room room = new Room();
        room.setName("Sala 1");
        room.setCapacity(100);
        room.setType("Normal");
        roomRepository.save(room);

        Room foundRoom = roomRepository.findById(room.getId()).orElse(null);

        assertThat(foundRoom).isNotNull();
        assertThat(foundRoom.getName()).isEqualTo("Sala 1");
    }

    @Test
    void testDeleteRoom() {
        Room room = new Room();
        room.setName("Sala 1");
        room.setCapacity(100);
        room.setType("Normal");
        roomRepository.save(room);

        roomRepository.deleteById(room.getId());

        assertThat(roomRepository.findById(room.getId())).isEmpty();
    }
}
