package deti.tqs.cinemax.IT;

import deti.tqs.cinemax.models.Movie;
import deti.tqs.cinemax.services.MovieService;
import deti.tqs.cinemax.services.RoomService;
import deti.tqs.cinemax.services.SessionService;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class DataInit implements CommandLineRunner {

    private MovieService movieService;

    private RoomService roomService;

    private SessionService sessionService;

    @Autowired
    public DataInit(MovieService movieService, RoomService roomService, SessionService sessionService) {
        this.movieService = movieService;
        this.roomService = roomService;
        this.sessionService = sessionService;
    }

    public void run(String... args) throws Exception {
        this.loadMovies();
    }

    private void loadMovies(){
        Movie movie1 = new Movie();
        movie1.setId(1L);
        movie1.setTitle("Inception");
        movie1.setCategory("Science Fiction");
        movie1.setGenre("Action");
        movie1.setStudio("Warner Bros.");
        movie1.setDuration("148 minutes");

        movieService.saveMovie(movie1);

        Movie movie2 = new Movie();
        movie2.setId(2L);

        movie2.setTitle("The Shawshank Redemption");
        movie2.setCategory("Drama");
        movie2.setGenre("Crime");
        movie2.setStudio("Castle Rock Entertainment");
        movie2.setDuration("142 minutes");

        movieService.saveMovie(movie2);

        Movie movie3 = new Movie();
        movie3.setId(3L);

        movie3.setTitle("The Godfather");
        movie3.setCategory("Crime");
        movie3.setGenre("Drama");
        movie3.setStudio("Paramount Pictures");
        movie3.setDuration("175 minutes");

        movieService.saveMovie(movie3);

    }
}
