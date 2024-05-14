package deti.tqs.cinemax.controllers;

import deti.tqs.cinemax.models.Room;
import deti.tqs.cinemax.services.roomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@Tag(name = "rooms", description = "Endpoints to manage rooms")
public class RoomController {

    private final roomService roomService;

    public RoomController(roomService roomService) {
        this.roomService = roomService;
    }

    @Operation(summary = "Get all rooms")
    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @Operation(summary = "Get room by id")
    @GetMapping("{id}")
    public Room getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id);
    }

    @Operation(summary = "Create room")
    @PostMapping
    public Room saveRoom(Room room) {
        return roomService.saveRoom(room);
    }



}
