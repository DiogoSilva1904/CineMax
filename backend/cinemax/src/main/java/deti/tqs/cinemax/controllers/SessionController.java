package deti.tqs.cinemax.controllers;

import deti.tqs.cinemax.models.Seat;
import deti.tqs.cinemax.services.seatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/session")
@Tag(name = "session", description = "Endpoints to manage session")
public class SessionController {

    private final seatService seatService;

    public SessionController(seatService seatService) {
        this.seatService = seatService;
    }

    @Operation(summary = "Get all seats")
    @GetMapping
    public List<Seat> getAllSeats() {
        return seatService.getAllSeats();
    }

    @Operation(summary = "Get seat by id")
    @GetMapping("{id}")
    public Seat getSeatById(@PathVariable Long id) {
        return seatService.getSeatById(id);
    }

    @PostMapping
    public Seat saveSeat(Seat seat) {
        return seatService.saveSeat(seat);
    }
}
