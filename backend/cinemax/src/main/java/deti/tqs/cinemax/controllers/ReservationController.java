package deti.tqs.cinemax.controllers;

import deti.tqs.cinemax.models.Reservation;
import deti.tqs.cinemax.services.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "reservations", description = "Endpoints to manage reservations")
public class ReservationController {

        private final ReservationService reservationService;

        public ReservationController(ReservationService reservationService){
            this.reservationService = reservationService;
        }

        @Operation(summary = "Get reservation by id")
        @GetMapping("{id}")
        public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
            Reservation reservation = reservationService.getReservationById(id);
            if(reservation == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        }

        @PostMapping
        public ResponseEntity<Reservation> saveReservation(@RequestBody Reservation reservation) {
            Reservation updatedReservation = reservationService.saveReservation(reservation);
            return new ResponseEntity<>(updatedReservation, HttpStatus.CREATED);

        }
}
