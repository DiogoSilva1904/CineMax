package deti.tqs.cinemax.services;

import deti.tqs.cinemax.models.Movie;
import deti.tqs.cinemax.models.MovieClass;
import deti.tqs.cinemax.repositories.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.method.P;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@Slf4j
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;    


    @Test
    void testGetAllMovies() {
        List<Movie> expectedMovies = new ArrayList<>();
        expectedMovies.add(new Movie(null, "Test Movie 1", "Action", "Thriller", "Studio X", "120min",null, null));
        expectedMovies.add(new Movie(null, "Test Movie 2", "Comedy", "Romance", "Studio Y", "100min",null, null));

        Mockito.when(movieRepository.findAll()).thenReturn(expectedMovies);

        log.info("Mocking movieRepository.findAll() to return {} movies", expectedMovies.size());

        List<Movie> actualMovies = movieService.getAllMovies();

        assertEquals(expectedMovies.size(), actualMovies.size());
        for (int i = 0; i < expectedMovies.size(); i++) {
            assertEquals(expectedMovies.get(i).getTitle(), actualMovies.get(i).getTitle());
            assertEquals(expectedMovies.get(i).getCategory(), actualMovies.get(i).getCategory());
        }
    }

    @Test
    void testGetMovieById_Found() {
        Long id = 1L;
        Movie expectedMovie = new Movie(id, "Test Movie", "Action", "Adventure", "Studio Z", "150min",null, null);

        Mockito.when(movieRepository.findById(id)).thenReturn(Optional.of(expectedMovie));

        log.info("Calling movieService.getMovieById(id={})", id);

        Movie actualMovie = movieService.getMovieById(id);

        assertNotNull(actualMovie);
        assertEquals(expectedMovie.getId(), actualMovie.getId());
        assertEquals(expectedMovie.getTitle(), actualMovie.getTitle());
        log.info("Retrieved movie with id {}: {}", id, actualMovie);
    }

    @Test
    void testGetMovieById_NotFound() {
        Long id = 1L;

        Mockito.when(movieRepository.findById(id)).thenReturn(Optional.empty());

        log.info("Calling movieService.getMovieById(id={})", id);

        Movie actualMovie = movieService.getMovieById(id);

        assertNull(actualMovie);
        log.info("Movie with id {} not found", id);
    }

    @Test
    void testSaveMovie() {
        Movie newMovie = new Movie(null, "New Movie", "Sci-Fi", "Drama", "Studio A", "180min",null, null);

        Mockito.when(movieRepository.save(newMovie)).thenReturn(newMovie);

        log.info("Calling movieService.saveMovie(movie={})", newMovie);

        Movie savedMovie = movieService.saveMovie(newMovie);

        assertNotNull(savedMovie);
        assertEquals(newMovie.getId(), savedMovie.getId());
        assertEquals(newMovie.getTitle(), savedMovie.getTitle());
        log.info("Saved movie: {}", savedMovie);
    }

    @Test
    public void testDeleteMovie_Success() throws IOException {
        Long movieId = 100L;
        String UserDir = System.getProperty("user.dir");
        String imagePath = "movie_poster.jpg";
        Movie movie = new Movie(movieId, "Test Movie", "Action", "Adventure", "Studio Z", "150min", imagePath, null);

        Path uploadDirPath = Paths.get(UserDir, "uploads");
        Files.createDirectories(uploadDirPath); 
        Path filePath = uploadDirPath.resolve(imagePath);
        Files.write(filePath, "test content".getBytes()); 

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        movieService.deleteMovie(movieId);

        verify(movieRepository, times(1)).deleteById(movieId);
        assertFalse(Files.exists(filePath));
    }
    

    @Test
    public void testDeleteMovie_NoImage() {
        Long movieId = 200L;
        Movie movie = new Movie(movieId, "Test Movie", "Action", "Adventure", "Studio Z", "150min", null, null);
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        movieService.deleteMovie(movieId);

        verify(movieRepository, times(1)).deleteById(movieId);
    }


    @Test
    void testUpdateMovie_Found() {
        Long id = 3L;
        Movie existingMovie = new Movie(null,"Existing Movie", "Animation", "Family", "Studio B", "90min",null, null);
        Movie updatedMovie = new Movie(null,"Updated Movie", "Comedy", "Family", "Studio B", "90min",null, null);

        Mockito.when(movieRepository.findById(id)).thenReturn(Optional.of(existingMovie));
        Mockito.when(movieRepository.save(updatedMovie)).thenReturn(updatedMovie);

        Optional<Movie> updatedOptionalMovie = movieService.updateMovie(id, updatedMovie);

        assertTrue(updatedOptionalMovie.isPresent());
        Movie actualMovie = updatedOptionalMovie.get();

        assertEquals(id, actualMovie.getId());
        assertEquals(updatedMovie.getTitle(), actualMovie.getTitle());
        assertEquals(updatedMovie.getCategory(), actualMovie.getCategory());
        log.info("Updated movie with id {}", id);
    }

    @Test
    void testSaveMovieWithTitleThatAlreadyExists(){
        Movie existingMovie = new Movie(1L, "Existing Movie", "Animation", "Family", "Studio B", "90min", null,null);

        Mockito.when(movieRepository.findByTitle(existingMovie.getTitle())).thenReturn(Optional.of(existingMovie));

        log.info("Calling movieService.saveMovie(movie={})", existingMovie);

        assertNull(movieService.saveMovie(existingMovie));
        log.info("Movie with title {} already exists", existingMovie.getTitle());
    }

    @Test
    void testCreateMovie() {
        MultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "image".getBytes());

        MovieClass newMovie = new MovieClass("New Movie1",  "180", "Studio A",  "Sci-Fi", image);

        Movie savedMovie = new Movie();
        savedMovie.setTitle(newMovie.getTitle());
        savedMovie.setDuration(newMovie.getDuration());
        savedMovie.setStudio(newMovie.getStudio());
        savedMovie.setGenre(newMovie.getGenre());
        savedMovie.setImagePath("image.jpg");

        Mockito.when(movieRepository.findByTitle(newMovie.getTitle())).thenReturn(Optional.empty());
        Mockito.when(movieRepository.save(any(Movie.class))).thenReturn(savedMovie);

        log.info("Calling movieService.CreateMovie(movie={})", newMovie);

        Movie returnedMovie = movieService.CreateMovie(newMovie);
        File file = new File(System.getProperty("user.dir") + "/uploads/" + savedMovie.getImagePath());


        assertNotNull(returnedMovie);
        assertEquals(newMovie.getTitle(), returnedMovie.getTitle());
        assertEquals(true, file.exists());
        file.delete();
        assertFalse(file.exists());
        log.info("Created movie: {}", savedMovie);
    }


    @Test
    public void testDetermineMediaType_Jpeg() {
        Path imagePath = mock(Path.class);
        when(imagePath.getFileName()).thenReturn(Paths.get("image.jpg"));

    
        MediaType mediaType = movieService.determineMediaType(imagePath);
        assertEquals(MediaType.IMAGE_JPEG, mediaType);
    }

    @Test
    public void testDetermineMediaType_Png() {
        Path imagePath = mock(Path.class);
        when(imagePath.getFileName()).thenReturn(Paths.get("image.png"));
        
    
        MediaType mediaType = movieService.determineMediaType(imagePath);
        assertEquals(MediaType.IMAGE_PNG, mediaType);
    }

    @Test
    public void testDetermineMediaType_Gif() {
        Path imagePath = mock(Path.class);
        when(imagePath.getFileName()).thenReturn(Paths.get("image.gif")); 
    
        MediaType mediaType = movieService.determineMediaType(imagePath);
        assertEquals(MediaType.IMAGE_GIF, mediaType);
    }

    @Test
    public void testDetermineMediaType_Other() {
        Path imagePath = mock(Path.class);
        when(imagePath.getFileName()).thenReturn(Paths.get("image")); 
    
        MediaType mediaType = movieService.determineMediaType(imagePath);
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, mediaType);
    }

    @Test
    public void testDetermineMediaType_Invalid() {
        Path imagePath = mock(Path.class);
        when(imagePath.getFileName()).thenReturn(Paths.get("image.invalid")); 
    
        MediaType mediaType = movieService.determineMediaType(imagePath);
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, mediaType);
    }
}
