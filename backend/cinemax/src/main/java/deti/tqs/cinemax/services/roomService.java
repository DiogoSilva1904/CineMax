package deti.tqs.cinemax.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import deti.tqs.cinemax.models.Room;
import deti.tqs.cinemax.repositories.roomRepository;

@Service
public class roomService {

    private final roomRepository roomRepository;

    private final Logger logger = LoggerFactory.getLogger(roomService.class);

    public roomService(roomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void deleteRoom(Long id) {
        logger.info("Deleting room with id {}", id);
        roomRepository.deleteById(id);
    }

    public Optional<Room> updateRoom(Long id, Room room) {
        Optional<Room> roomOptional = roomRepository.findById(id);
        if (roomOptional.isPresent()) {
            logger.info("Updating room with id {}", id);
            room.setId(id);
            return Optional.of(roomRepository.save(room));
        }
        else {
            logger.info("Room with id {} not found", id);
        }

        return roomOptional;
    } 
    
    public Room saveRoom(Room room) {
        logger.info("Saving room with id {}", room.getId());
        return roomRepository.save(room);
    }

    public Room getRoomById(Long id) {
        logger.info("Retrieving room with id {}", id);
        return roomRepository.findById(id).orElse(null);
    }

    public List<Room> getAllRooms() {
        logger.info("Retrieving all rooms");
        return roomRepository.findAll();
    }
    
}
