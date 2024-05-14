package deti.tqs.cinemax.controllers;

import deti.tqs.cinemax.models.Seat;
import deti.tqs.cinemax.services.SeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/seats")
@Tag(name = "seats", description = "Endpoints to manage seats")
public class SeatController {

        private final SeatService seatService;

        public SeatController(SeatService seatService) {
            this.seatService = seatService;
        }

        @Operation(summary = "Get all seats")
        @GetMapping
        public ResponseEntity<List<Seat>> getAllSeats() {
            List<Seat> seats = seatService.getAllSeats();
            return new ResponseEntity<>(seats, HttpStatus.OK);
        }

        @Operation(summary = "Get seat by id")
        @GetMapping("{id}")
        public ResponseEntity<Seat> getSeatById(@PathVariable Long id) {
            Seat seat = seatService.getSeatById(id);
            if(seat == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(seat, HttpStatus.OK);
        }

        @PostMapping
        public ResponseEntity<Seat> saveSeat(@RequestBody Seat seat) {
            Seat updatedSeat = seatService.saveSeat(seat);
            return new ResponseEntity<>(updatedSeat, HttpStatus.CREATED);
        }
}
