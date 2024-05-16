package deti.tqs.cinemax.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import deti.tqs.cinemax.models.Movie;
import deti.tqs.cinemax.services.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "localhost:4200")
@RestController
@RequestMapping("/api/movies")
@Tag(name = "movies", description = "Endpoints to manage movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @Operation(summary = "Get all movies")
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Operation(summary = "Get movie by id")
    @GetMapping("{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Movie movie = movieService.getMovieById(id);
        if(movie == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Movie> saveMovie(@RequestBody Movie movie) {
        Movie updatedmovie = movieService.saveMovie(movie);
        return new ResponseEntity<>(updatedmovie, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    
    
}
