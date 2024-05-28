package deti.tqs.cinemax.dataInit;

import deti.tqs.cinemax.models.AppUser;
import deti.tqs.cinemax.models.Movie;
import deti.tqs.cinemax.services.MovieService;
import deti.tqs.cinemax.services.RoomService;
import deti.tqs.cinemax.services.SessionService;
import deti.tqs.cinemax.services.UserService;
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

    private UserService userService;

    @Autowired
    public DataInit(MovieService movieService, RoomService roomService, SessionService sessionService, UserService userService) {
        this.movieService = movieService;
        this.roomService = roomService;
        this.sessionService = sessionService;
        this.userService = userService;
    }

    public void run(String... args) throws Exception {
        this.loadMovies();
        this.loadUser();
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

    private void loadUser(){
        AppUser user = new AppUser();
        user.setId(1L);
        user.setRole("ADMIN");
        user.setPassword("admin");
        user.setEmail("dio@gmail.com");
        user.setUsername("admin");

        userService.saveUser(user);
    }
}
