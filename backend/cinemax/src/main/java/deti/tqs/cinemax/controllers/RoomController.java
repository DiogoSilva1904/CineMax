package deti.tqs.cinemax.controllers;

import deti.tqs.cinemax.models.Room;
import deti.tqs.cinemax.services.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@Tag(name = "rooms", description = "Endpoints to manage rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Operation(summary = "Get all rooms")
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @Operation(summary = "Get room by id")
    @GetMapping("{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        Room room = roomService.getRoomById(id);
        if(room == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @Operation(summary = "Create room")
    @PostMapping
    public ResponseEntity<Room> saveRoom(@RequestBody Room room) {
        Room updatedRoom = roomService.saveRoom(room);
        return new ResponseEntity<>(updatedRoom, HttpStatus.CREATED);
    }



}
