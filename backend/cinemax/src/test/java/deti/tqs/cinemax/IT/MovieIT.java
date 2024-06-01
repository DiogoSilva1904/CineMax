package deti.tqs.cinemax.IT;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import deti.tqs.cinemax.config.CustomUserDetailsService;
import deti.tqs.cinemax.config.JwtAuthFilter;
import deti.tqs.cinemax.models.Movie;
import org.apache.coyote.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.profiles.active=test"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MovieIT {

    @Container
    public static GenericContainer container = new GenericContainer("mysql:latest")
        .withExposedPorts(3306)
        .withEnv("MYSQL_ROOT_PASSWORD", "rootpass")
        .withEnv("MYSQL_DATABASE", "cinemax")
        .withEnv("MYSQL_USER", "user")
        .withEnv("MYSQL_PASSWORD", "secret");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        String jdbcUrl = "jdbc:mysql://" + container.getHost() + ":" + container.getMappedPort(3306) + "/cinemax";
        registry.add("spring.datasource.url", () -> jdbcUrl);
        registry.add("spring.datasource.username", () -> "user");
        registry.add("spring.datasource.password", () -> "secret");
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static String jwtToken;
    private static final TestRestTemplate restTemplate1 = new TestRestTemplate();

    @BeforeAll
    static void setup(@LocalServerPort int port1) {
        String requestBody = "{\"username\":\"" + "admin" + "\",\"password\":\"" + "admin" + "\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate1.exchange(
                "http://localhost:"+ port1 + "/api/login",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        assertEquals(200, response.getStatusCodeValue());

        String responseBody = response.getBody();

        System.out.println(responseBody);

        JsonElement jsonElement = JsonParser.parseString(responseBody);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        jwtToken = jsonObject.get("jwt").getAsString();
    }

    @Test
    @Order(1)
    void testGetAllMovies() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<List<Movie>> response = restTemplate.exchange("http://localhost:" + port + "/api/movies", HttpMethod.GET ,entity,new ParameterizedTypeReference<List<Movie>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Movie> movies = response.getBody();
        assertEquals(3, movies.size());
    }

    @Test
    @Order(2)
    void testGetMovieById() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Movie> response = restTemplate.exchange("http://localhost:" + port + "/api/movies/1", HttpMethod.GET ,entity, Movie.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Movie movie = response.getBody();
        assertEquals("Inception", movie.getTitle());
        assertEquals("Science Fiction", movie.getCategory());
    }

    @Test
    @Order(3)
    void testGetMovieByIdFailure() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Movie> response = restTemplate.exchange("http://localhost:" + port + "/api/movies/5", HttpMethod.GET ,entity, Movie.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(4)
    @Disabled("is not working")
    void testSaveMovie(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Movie movie = new Movie();
        movie.setTitle("The Dark Knight");
        movie.setCategory("Action");
        movie.setGenre("Crime");
        movie.setStudio("Warner Bros.");
        movie.setDuration("152 minutes");
        movie.setImagePath(null);

        HttpEntity<Movie> entity = new HttpEntity<>(movie, headers);

        ResponseEntity<Movie> response = restTemplate.exchange("http://localhost:" + port + "/api/movies", HttpMethod.POST ,entity, Movie.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Movie savedMovie = response.getBody();
        assertEquals("The Dark Knight", savedMovie.getTitle());
        assertEquals("Action", savedMovie.getCategory());

    }

    @Test
    @Order(5)
    @Disabled("missing logic")
    void testSavingMovieAlreadyExists(){//could need a change on final assert
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setCategory("Science Fiction");
        movie.setGenre("Action");
        movie.setStudio("Warner Bros.");
        movie.setDuration("148 minutes");

        HttpEntity<Movie> entity = new HttpEntity<>(movie, headers);

        ResponseEntity<Movie> response = restTemplate.exchange("http://localhost:" + port + "/api/movies", HttpMethod.POST ,entity, Movie.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
