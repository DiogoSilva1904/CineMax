package deti.tqs.cinemax.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deti.tqs.cinemax.models.movie;
import deti.tqs.cinemax.services.movieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/movies")
@Tag(name = "movies", description = "Endpoints to manage movies")
public class movieController {

    private final movieService movieService;

    public movieController(movieService movieService) {
        this.movieService = movieService;
    }

    @Operation(summary = "Get all movies")
    @GetMapping
    public List<movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @PostMapping
    public movie saveMovie(movie movie) {
        return movieService.saveMovie(movie);
    }

    
    
}
