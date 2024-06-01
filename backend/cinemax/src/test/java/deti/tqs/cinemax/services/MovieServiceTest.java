package deti.tqs.cinemax.services;

import deti.tqs.cinemax.models.Movie;
import deti.tqs.cinemax.repositories.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
    void testDeleteMovie() {
        Long id = 2L;

        // No need to mock anything for delete, as it doesn't return a value
        movieService.deleteMovie(id);

        log.info("Calling movieService.deleteMovie(id={})", id);

        Mockito.verify(movieRepository).deleteById(id);
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

        assertThrows(IllegalArgumentException.class, () -> movieService.saveMovie(existingMovie));
        log.info("Movie with title {} already exists", existingMovie.getTitle());
    }
}
