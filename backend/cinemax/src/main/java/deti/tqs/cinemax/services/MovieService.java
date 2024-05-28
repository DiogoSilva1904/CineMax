package deti.tqs.cinemax.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import deti.tqs.cinemax.models.CustomFile;
import deti.tqs.cinemax.models.Movie;
import deti.tqs.cinemax.models.MovieClass;
import deti.tqs.cinemax.repositories.MovieRepository;

@Service
public class MovieService {
    
    private final MovieRepository movieRepository;
    private static final Logger log = LoggerFactory.getLogger(MovieService.class);
    private static final String USERDIR = System.getProperty("user.dir");

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

    public Movie CreateMovie(MovieClass movie) {
        log.info("Creating movie with title {}", movie.getTitle());
        Movie newMovie = new Movie();
        newMovie.setTitle(movie.getTitle());
        newMovie.setDuration(movie.getDuration());
        newMovie.setStudio(movie.getStudio());
        newMovie.setGenre(movie.getGenre());
    
        String imagePath = movie.getImage() != null ? CreateFile(movie) : null;
        newMovie.setImagePath(imagePath);
    
        Movie savedMovie = movieRepository.save(newMovie);
        if (savedMovie == null) {
            log.error("Failed to save movie");
        }
        return savedMovie;
    }
    


    public String CreateFile(MovieClass movie) {
        if (movie.getImage() == null || movie.getImage().isEmpty()) {
            log.info("Provided file is empty or null.");
            return "File is empty or null.";
        }
    
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(movie.getImage().getOriginalFilename()));
        Path uploadDirPath = Paths.get(USERDIR, "uploads");
    
        try {
            if (!Files.exists(uploadDirPath)) {
                Files.createDirectories(uploadDirPath);
            }
    
            Path fileSysPath = uploadDirPath.resolve(fileName);
    
            // Copy the file to the target location, replacing existing file if it exists
            Files.copy(movie.getImage().getInputStream(), fileSysPath, StandardCopyOption.REPLACE_EXISTING);
            return fileSysPath.toString();
        } catch (IOException e) {
            log.error("Failed to save file: {}", e.getMessage(), e);
            return "Failed to save file.";
        }
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
