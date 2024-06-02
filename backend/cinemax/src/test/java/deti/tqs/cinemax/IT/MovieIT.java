package deti.tqs.cinemax.IT;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import deti.tqs.cinemax.models.Movie;
import deti.tqs.cinemax.models.MovieClass;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.http.*;

import java.nio.charset.StandardCharsets;
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

        assertEquals(HttpStatus.OK, response.getStatusCode());

        String responseBody = response.getBody();

        System.out.println(responseBody);

        JsonElement jsonElement = JsonParser.parseString(responseBody);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        jwtToken = jsonObject.get("jwt").getAsString();
    }

    @Test
    @Order(1)
    void whenGetAllMovies_ReturnAllMovies() {
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
    void whenGetMovieById_ReturnMovie() {
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
    void whenGetMovieById_ReturnNotFound() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Movie> response = restTemplate.exchange("http://localhost:" + port + "/api/movies/5", HttpMethod.GET ,entity, Movie.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(4)
    @Disabled("missing logic")
    void whenCreateMovie_ReturnCorrectResponse() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        byte[] content = "This is a test file content".getBytes(StandardCharsets.UTF_8);
        MultipartFile file = new MockMultipartFile("image", "test.png", "image/png", content);

        MovieClass movie = new MovieClass();
        movie.setTitle("The Dark Knight");
        movie.setGenre("Crime");
        movie.setStudio("Warner Bros.");
        movie.setDuration("152 minutes");

        // Convert the movie object to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String movieJson = objectMapper.writeValueAsString(movie);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(content) {
            @Override
            public String getFilename() {
                return "test.png";
            }
        });
        body.add("movie", new HttpEntity<>(movieJson, headers));

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Movie> response = restTemplate.exchange("http://localhost:" + port + "/api/movies", HttpMethod.POST, entity, Movie.class);

        String responseJson = objectMapper.writeValueAsString(response.getBody());
        System.out.println("Response: " + responseJson);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Movie savedMovie = response.getBody();
        assertEquals("The Dark Knight", savedMovie.getTitle());
        assertEquals("Crime", savedMovie.getGenre()); // Ensure this matches the input
        assertEquals("Warner Bros.", savedMovie.getStudio());
        assertEquals("152 minutes", savedMovie.getDuration());
    }


    @Test
    @Order(5)
    @Disabled("missing logic")
    void whenCreateMovieWithAlreadyExistingTitle_ReturnBadRequest(){//could need a change on final assert
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
