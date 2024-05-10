package deti.tqs.cinemax.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import deti.tqs.cinemax.models.movie;
import deti.tqs.cinemax.repositories.movieRepository;

@Service
public class movieService {
    
    private final movieRepository movieRepository;
    private static final Logger log = LoggerFactory.getLogger(movieService.class);

    public movieService(movieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<movie> getAllMovies() {
        log.info("Retrieving all movies");
        return movieRepository.findAll();
    }

    public movie getMovieById(Long id) {
        log.info("Retrieving movie with id {}", id);
        return movieRepository.findById(id).orElse(null);
    }

    public movie saveMovie(movie movie) {
        log.info("Saving movie with id {}", movie.getId());
        return movieRepository.save(movie);
    }


    public void deleteMovie(Long id) {
        log.info("Deleting movie with id {}", id);
        movieRepository.deleteById(id);
    }


    public Optional<movie> updateMovie(Long id, movie movie) {
        
        Optional<movie> movieOptional = movieRepository.findById(id);

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
