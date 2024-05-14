package deti.tqs.cinemax.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import deti.tqs.cinemax.models.Movie;
import deti.tqs.cinemax.services.movieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/movies")
@Tag(name = "movies", description = "Endpoints to manage movies")
public class MovieController {

    private final movieService movieService;

    public MovieController(movieService movieService) {
        this.movieService = movieService;
    }

    @Operation(summary = "Get all movies")
    @GetMapping
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @Operation(summary = "Get movie by id")
    @GetMapping("{id}")
    public Movie getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    @PostMapping
    public Movie saveMovie(Movie movie) {
        return movieService.saveMovie(movie);
    }

    
    
}
