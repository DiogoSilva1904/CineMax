package deti.tqs.cinemax.services;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import deti.tqs.cinemax.models.Movie;
import deti.tqs.cinemax.models.MovieClass;
import deti.tqs.cinemax.repositories.MovieRepository;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private static final Logger log = LoggerFactory.getLogger(MovieService.class);
    private static final String USERDIR = System.getProperty("user.dir");

    private static final String UPLOAD_PATH = "uploads";


    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * Retrieves all movies from the repository.
     *
     * @return a list of all movies.
     */
    public List<Movie> getAllMovies() {
        log.info("Retrieving all movies");
        return movieRepository.findAll();
    }

    /**
     * Retrieves a movie by its ID.
     *
     * @param id the ID of the movie.
     * @return the movie with the specified ID, or null if not found.
     */
    public Movie getMovieById(Long id) {
        log.info("Retrieving movie with id {}", id);
        return movieRepository.findById(id).orElse(null);
    }

    /**
     * Saves a movie to the repository.
     *
     * @param movie the movie to be saved.
     * @return the saved movie, or null if a movie with the same title already exists.
     */
    public Movie saveMovie(Movie movie) {
        log.info("Saving movie with id {}", movie.getId());
        if (movieRepository.findByTitle(movie.getTitle()).isPresent()) {
            log.info("Movie with title {} already exists", movie.getTitle());
            return null;
        }
        return movieRepository.save(movie);
    }

    /**
     * Creates a new movie from a MovieClass instance and saves it to the repository.
     *
     * @param movie the MovieClass instance containing the movie details.
     * @return the created and saved movie, or null if a movie with the same title already exists.
     */
    public Movie CreateMovie(MovieClass movie) {
        log.info("Creating movie with title {}", movie.getTitle());
        Optional<Movie> existingMovie = movieRepository.findByTitle(movie.getTitle());
        if (existingMovie.isPresent()) {
            log.info("Movie with title {} already exists", movie.getTitle());
            return null;
        }
        Movie newMovie = new Movie();
        newMovie.setTitle(movie.getTitle());
        newMovie.setDuration(movie.getDuration());
        newMovie.setStudio(movie.getStudio());
        newMovie.setGenre(movie.getGenre());

        String imagePath = movie.getImage() != null ? CreateFile(movie) : null;
        newMovie.setImagePath(imagePath);

        log.info("Saving movie: {}", newMovie);

        Movie savedMovie = movieRepository.save(newMovie);
        if (savedMovie == null) {
            log.error("Failed to save movie");
        }
        return savedMovie;
    }

    /**
     * Creates a file on the filesystem for the provided movie's image.
     *
     * @param movie the MovieClass instance containing the image.
     * @return the name of the saved file, or an error message if saving fails.
     */
    public String CreateFile(MovieClass movie) {
        if (movie.getImage() == null || movie.getImage().isEmpty()) {
            log.info("Provided file is empty or null.");
            return "File is empty or null.";
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(movie.getImage().getOriginalFilename()));
        Path uploadDirPath = Paths.get(USERDIR, UPLOAD_PATH);

        try {
            if (!Files.exists(uploadDirPath)) {
                Files.createDirectories(uploadDirPath);
            }

            Path fileSysPath = uploadDirPath.resolve(fileName);

            // Copy the file to the target location, replacing existing file if it exists
            Files.copy(movie.getImage().getInputStream(), fileSysPath, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            log.error("Failed to save file: {}", e.getMessage(), e);
            return "Failed to save file.";
        }
    }

    /**
     * Deletes a movie by its ID, including its associated image file if it exists.
     *
     * @param id the ID of the movie to delete.
     */
    public void deleteMovie(Long id) {
        log.info("Deleting movie with id {}", id);

        // Delete the image file
        Optional<Movie> movieOptional = movieRepository.findById(id);
        log.info("Movie optional: {}", movieOptional);
        if (movieOptional.isPresent()) {
            Movie movie = movieOptional.get();
            log.info("Movie: {}", movie.getImagePath());
            if (movie.getImagePath() != null) {
                Path uploadDirPath = Paths.get(USERDIR, UPLOAD_PATH);
                Path fileSysPath = uploadDirPath.resolve(movie.getImagePath());
                log.info("Deleting image file: {}", fileSysPath);
                try {
                    Files.deleteIfExists(fileSysPath);
                } catch (IOException e) {
                    log.error("Failed to delete image file: {}", e.getMessage(), e);
                }
            }
        }

        movieRepository.deleteById(id);
    }

    /**
     * Updates an existing movie.
     *
     * @param id    the ID of the movie to update.
     * @param movie the updated movie details.
     * @return an Optional containing the updated movie if successful, or an empty Optional if the movie was not found.
     */
    public Optional<Movie> updateMovie(Long id, Movie movie) {
        Optional<Movie> movieOptional = movieRepository.findById(id);

        if (movieOptional.isPresent()) {
            log.info("Updating movie with id {}", id);
            movie.setId(id);
            return Optional.of(movieRepository.save(movie));
        } else {
            log.info("Movie with id {} not found", id);
        }

        return movieOptional;
    }

    /**
     * Retrieves an image file as a ResponseEntity.
     *
     * @param imageP the name of the image file.
     * @return a ResponseEntity containing the image file as a ByteArrayResource or an error message if the file is not found.
     */
    public ResponseEntity<Object> getImage(String imageP) {
        try {
            log.info("Getting image from path: {}", imageP);

            Path uploadDirPath = Paths.get(USERDIR, UPLOAD_PATH);
            // add the file name
            String filePath = uploadDirPath + "/" + imageP;
            Path imagePath = Paths.get(filePath);

            // Check if the file exists
            if (!Files.exists(imagePath) || !Files.isRegularFile(imagePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Image not found");
            }

            // Read the image bytes using a buffered stream
            ByteArrayResource resource;
            try (InputStream inputStream = new BufferedInputStream(Files.newInputStream(imagePath))) {
                resource = new ByteArrayResource(inputStream.readAllBytes());
            }

            // Determine the content type dynamically based on the file extension
            MediaType mediaType = determineMediaType(imagePath);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (IOException e) {
            log.error("Error while reading image", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while reading image");
        }
    }

    /**
     * Determines the media type of a file based on its extension.
     *
     * @param imagePath the path of the image file.
     * @return the MediaType corresponding to the file extension, or MediaType.APPLICATION_OCTET_STREAM if the extension is unknown.
     */
    public MediaType determineMediaType(Path imagePath) {
        String fileName = imagePath.getFileName().toString().toLowerCase();
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (fileName.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (fileName.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

}
