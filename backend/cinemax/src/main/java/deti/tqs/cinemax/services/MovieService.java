package deti.tqs.cinemax.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import deti.tqs.cinemax.models.Movie;
import deti.tqs.cinemax.repositories.MovieRepository;

@Service
public class MovieService {
    
    private final MovieRepository movieRepository;
    private static final Logger log = LoggerFactory.getLogger(MovieService.class);

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies() {
        log.info("Retrieving all movies");
        return movieRepository.findAll();
    }

    public Movie getMovieById(Long id) {
        log.info("Retrieving movie with id {}", id);
        return movieRepository.findById(id).orElse(null);
    }

    public Movie saveMovie(Movie movie) {
        log.info("Saving movie with id {}", movie.getId());
        return movieRepository.save(movie);
    }


    public void deleteMovie(Long id) {
        log.info("Deleting movie with id {}", id);
        movieRepository.deleteById(id);
    }


    public Optional<Movie> updateMovie(Long id, Movie movie) {
        
        Optional<Movie> movieOptional = movieRepository.findById(id);

        if (movieOptional.isPresent()) {
            log.info("Updating movie with id {}", id);
            movie.setId(id);
            return Optional.of(movieRepository.save(movie));
        }
        else {
            log.info("Movie with id {} not found", id);
        }

        return movieOptional;
    }

}
