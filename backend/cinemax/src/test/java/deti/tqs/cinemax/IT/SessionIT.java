package deti.tqs.cinemax.IT;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import deti.tqs.cinemax.models.Movie;
import deti.tqs.cinemax.models.Room;
import deti.tqs.cinemax.models.Session;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.profiles.active=test"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SessionIT {
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
                "http://localhost:" + port1 + "/api/login",
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
    void testGetAllSessions(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<List<Session>> response = restTemplate.exchange("http://localhost:" + port + "/api/sessions", HttpMethod.GET, entity, new ParameterizedTypeReference<List<Session>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Session> sessions = response.getBody();

        assertEquals(3, sessions.size());
        assertEquals("2024-05-23", sessions.get(0).getDate());
        assertEquals("20:00", sessions.get(0).getTime());
        assertEquals("2024-05-16", sessions.get(1).getDate());
        assertEquals("20:00", sessions.get(1).getTime());
        assertEquals("2024-05-28", sessions.get(2).getDate());
        assertEquals("22:00", sessions.get(2).getTime());
    }

    @Test
    @Order(2)
    void testGetSessionById(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Session> response = restTemplate.exchange("http://localhost:" + port + "/api/sessions/1", HttpMethod.GET, entity, Session.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Session session = response.getBody();

        assertEquals("2024-05-23", session.getDate());
        assertEquals("20:00", session.getTime());
    }

    @Test
    @Order(3)
    void testGetSessionByIdNotFound(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Session> response = restTemplate.exchange("http://localhost:" + port + "/api/sessions/4", HttpMethod.GET, entity, Session.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(4)
    void testSaveSession(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        HttpEntity<?> entity1 = new HttpEntity<>(headers);

        ResponseEntity<Room> response = restTemplate.exchange("http://localhost:" + port + "/api/rooms/1", HttpMethod.GET, entity1, Room.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Room room = response.getBody();

        ResponseEntity<Movie> response2 = restTemplate.exchange("http://localhost:" + port + "/api/movies/1", HttpMethod.GET, entity1, Movie.class);
        Movie movie = response2.getBody();

        Session session = new Session();
        session.setDate("2024-05-23");
        session.setTime("20:00");
        session.setRoom(room);
        session.setBookedSeats(List.of("C1", "C2"));
        session.setMovie(movie);

        HttpEntity<Session> entity = new HttpEntity<>(session, headers);

        ResponseEntity<Session> response1 = restTemplate.exchange("http://localhost:" + port + "/api/sessions", HttpMethod.POST, entity, Session.class);

        assertEquals(HttpStatus.CREATED, response1.getStatusCode());

        Session savedSession = response1.getBody();

        assertEquals("2024-05-23", savedSession.getDate());
        assertEquals("20:00", savedSession.getTime());
        assertEquals("Room A", savedSession.getRoom().getName());
        assertEquals("C1", savedSession.getBookedSeats().get(0));
        assertEquals("C2", savedSession.getBookedSeats().get(1));
        assertEquals("Inception", savedSession.getMovie().getTitle());
    }

    @Test
    @Order(5)
    @Disabled
    void testGetSessionByDate(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<List<Session>> response = restTemplate.exchange("http://localhost:" + port + "/api/sessions/date/2024-05-23", HttpMethod.GET, entity, new ParameterizedTypeReference<List<Session>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Session> sessions = response.getBody();

        assertEquals(1, sessions.size());
        assertEquals("2024-05-23", sessions.get(0).getDate());
        assertEquals("20:00", sessions.get(0).getTime());
    }

    @Test
    @Order(6)
    void testDeleteSession(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + port + "/api/sessions/1", HttpMethod.DELETE, entity, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
