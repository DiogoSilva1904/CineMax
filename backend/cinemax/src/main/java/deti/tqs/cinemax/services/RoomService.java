package deti.tqs.cinemax.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import deti.tqs.cinemax.models.Room;
import deti.tqs.cinemax.repositories.RoomRepository;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    private final Logger logger = LoggerFactory.getLogger(RoomService.class);

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /**
     * Deletes a room by its ID.
     *
     * @param id the ID of the room to delete.
     */
    public void deleteRoom(Long id) {
        logger.info("Deleting room with id {}", id);
        roomRepository.deleteById(id);
    }

    /**
     * Updates an existing room.
     *
     * @param id   the ID of the room to update.
     * @param room the updated room details.
     * @return an Optional containing the updated room if successful, or an empty Optional if the room was not found.
     */
    public Optional<Room> updateRoom(Long id, Room room) {
        Optional<Room> roomOptional = roomRepository.findById(id);
        if (roomOptional.isPresent()) {
            logger.info("Updating room with id {}", id);
            room.setId(id);
            return Optional.of(roomRepository.save(room));
        } else {
            logger.info("Room with id {} not found", id);
        }
        return roomOptional;
    }

    /**
     * Saves a room to the repository.
     *
     * @param room the room to save.
     * @return the saved room.
     */
    public Room saveRoom(Room room) {
        logger.info("Saving room with id {}", room.getId());
        return roomRepository.save(room);
    }

    /**
     * Retrieves a room by its ID.
     *
     * @param id the ID of the room.
     * @return the room with the specified ID, or null if not found.
     */
    public Room getRoomById(Long id) {
        logger.info("Retrieving room with id {}", id);
        return roomRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves all rooms from the repository.
     *
     * @return a list of all rooms.
     */
    public List<Room> getAllRooms() {
        logger.info("Retrieving all rooms");
        return roomRepository.findAll();
    }

}
