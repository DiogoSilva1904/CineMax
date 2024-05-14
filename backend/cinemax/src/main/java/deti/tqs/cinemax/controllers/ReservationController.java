package deti.tqs.cinemax.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import deti.tqs.cinemax.models.Movie;
import deti.tqs.cinemax.services.movieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "reservations", description = "Endpoints to manage reservations")
public class ReservationController {

        private final movieService movieService;

        public ReservationController(movieService movieService) {
            this.movieService = movieService;
        }

        @Operation(summary = "Get all reservations")
        @GetMapping
        public List<Movie> getAllReservations() {
            return movieService.getAllMovies();
        }

        @Operation(summary = "Get reservation by id")
        @GetMapping("{id}")
        public Movie getReservationById(@PathVariable Long id) {
            return movieService.getMovieById(id);
        }

        @PostMapping
        public Movie saveReservation(Movie movie) {
            return movieService.saveMovie(movie);
        }
}
