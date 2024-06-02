package deti.tqs.cinemax.controllers;

import deti.tqs.cinemax.config.CustomUserDetailsService;
import deti.tqs.cinemax.config.IAuthenticationFacade;
import deti.tqs.cinemax.config.JwtUtilService;
import deti.tqs.cinemax.models.Movie;
import deti.tqs.cinemax.repositories.MovieRepository;
import deti.tqs.cinemax.services.MovieService;

import org.junit.Rule;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.http.MediaType;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;

@WebMvcTest(MovieController.class)
@AutoConfigureMockMvc(addFilters = false)
class MovieControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtUtilService jwtUtil;

    @MockBean
    private IAuthenticationFacade authenticationFacade;

    @MockBean
    private MovieService movieService;

    @MockBean
    private MovieRepository movieRepository;

    private ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void testGetAllMovies() throws Exception{
        Movie movie1 = new Movie();
        movie1.setId(1L);
        movie1.setTitle("Oppenheimer");
        movie1.setCategory("Action");
        movie1.setGenre("Thriller");
        movie1.setStudio("Studio X");
        movie1.setDuration("120min");

        Movie movie2 = new Movie();
        movie2.setId(2L);
        movie2.setTitle("Nope");
        movie2.setCategory("Comedy");
        movie2.setGenre("Romance");
        movie2.setStudio("Studio Y");
        movie2.setDuration("100min");

        List<Movie> expectedMovies = List.of(movie1, movie2);

        when(movieService.getAllMovies()).thenReturn(expectedMovies);

       mvc.perform(get("/api/movies"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].title", is("Oppenheimer")))
               .andExpect(jsonPath("$[1].title", is("Nope")));
    }

    @Test
    void testGetMovieByID() throws Exception{
        Movie movie1 = new Movie();
        movie1.setId(1L);
        movie1.setTitle("Oppenheimer");
        movie1.setCategory("Action");
        movie1.setGenre("Thriller");
        movie1.setStudio("Studio X");
        movie1.setDuration("120min");

        when(movieService.getMovieById(1L)).thenReturn(movie1);

        mvc.perform(get("/api/movies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Oppenheimer")))
                .andExpect(jsonPath("$.category", is("Action")))
                .andExpect(jsonPath("$.genre", is("Thriller")))
                .andExpect(jsonPath("$.studio", is("Studio X")))
                .andExpect(jsonPath("$.duration", is("120min")));


    }

    @Test
    void testGetMovieByIDFailure() throws Exception{
        when(movieService.getMovieById(1L)).thenReturn(null);

        mvc.perform(get("/api/movies/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSaveMovie() throws Exception{
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Oppenheimer");
        movie.setCategory("Action");
        movie.setGenre("Thriller");
        movie.setStudio("Studio X");
        movie.setDuration("120min");

        Mockito.when(movieService.CreateMovie(Mockito.any())).thenReturn(movie);

        mvc.perform(post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Oppenheimer")))
                .andExpect(jsonPath("$.category", is("Action")))
                .andExpect(jsonPath("$.genre", is("Thriller")))
                .andExpect(jsonPath("$.studio", is("Studio X")))
                .andExpect(jsonPath("$.duration", is("120min")));
    }

    @Test
    @Disabled
    void testSaveMovieAlreadyExists() throws Exception {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Oppenheimer");
        movie.setCategory("Action");
        movie.setGenre("Thriller");
        movie.setStudio("Studio X");
        movie.setDuration("120min");

        // Mock the repository to return a movie when searching for the title "Oppenheimer"
        //when(movieRepository.findByTitle("Oppenheimer")).thenReturn();

        // Perform the POST request and expect a BAD_REQUEST status
        mvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().isBadRequest());
    }
}
