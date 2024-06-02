package deti.tqs.cinemax.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import deti.tqs.cinemax.models.Movie;
import deti.tqs.cinemax.repositories.MovieRepository;

@DataJpaTest
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void testSaveMovie() {
        Movie movie = new Movie();
        movie.setTitle("Movie 1");
        movie.setCategory("Action");
        movie.setGenre("Adventure");
        movie.setStudio("Studio XYZ");
        movie.setDuration("120 min");
        movie.setImagePath("path/to/image1");

        Movie savedMovie = movieRepository.save(movie);
        assertThat(savedMovie.getId()).isNotNull();
    }

    @Test
    void testFindById() {
        Movie movie = new Movie();
        movie.setTitle("Movie 1");
        movie.setCategory("Action");
        movie.setGenre("Adventure");
        movie.setStudio("Studio XYZ");
        movie.setDuration("120 min");
        movie.setImagePath("path/to/image1");

        Movie savedMovie = movieRepository.save(movie);
        Long movieId = savedMovie.getId();

        Optional<Movie> foundMovie = movieRepository.findById(movieId);
        assertThat(foundMovie).isPresent();
        assertThat(foundMovie.get().getTitle()).isEqualTo("Movie 1");
    }

    @Test
    void testFindAll() {
        Movie movie1 = new Movie();
        movie1.setTitle("Movie 1");
        movie1.setCategory("Action");
        movie1.setGenre("Adventure");
        movie1.setStudio("Studio XYZ");
        movie1.setDuration("120 min");
        movie1.setImagePath("path/to/image1");

        movieRepository.save(movie1);

        Movie movie2 = new Movie();
        movie2.setTitle("Movie 2");
        movie2.setCategory("Comedy");
        movie2.setGenre("Romance");
        movie2.setStudio("Studio ABC");
        movie2.setDuration("90 min");
        movie2.setImagePath("path/to/image2");

        movieRepository.save(movie2);

        List<Movie> movies = movieRepository.findAll();
        assertThat(movies).hasSize(2);
    }

    @Test
    void testDeleteMovie() {
        Movie movie = new Movie();
        movie.setTitle("Movie 1");
        movie.setCategory("Action");
        movie.setGenre("Adventure");
        movie.setStudio("Studio XYZ");
        movie.setDuration("120 min");
        movie.setImagePath("path/to/image1");

        Movie savedMovie = movieRepository.save(movie);
        Long movieId = savedMovie.getId();

        movieRepository.deleteById(movieId);
        assertThat(movieRepository.findById(movieId)).isEmpty();
    }
}
