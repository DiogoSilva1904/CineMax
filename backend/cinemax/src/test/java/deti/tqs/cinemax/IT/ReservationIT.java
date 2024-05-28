package deti.tqs.cinemax.IT;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import deti.tqs.cinemax.models.Reservation;
import deti.tqs.cinemax.models.Session;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
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
public class ReservationIT {

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

        JsonElement jsonElement = JsonParser.parseString(responseBody);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        jwtToken = jsonObject.get("jwt").getAsString();
    }

    @Test
    @Order(1)
    void testGetReservationById() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Reservation> response = restTemplate.exchange("http://localhost:" + port + "/api/reservations/1", HttpMethod.GET, entity, Reservation.class);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Reservation reservation = response.getBody();

        assertEquals(1, reservation.getId());
        assertEquals(10, reservation.getPrice());
        assertEquals(1,reservation.getSeatNumbers().size());
    }

    @Test
    @Order(3)
    void testSaveReservation() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        HttpEntity<?> entity1 = new HttpEntity<>(headers);

        ResponseEntity<Session> response = restTemplate.exchange("http://localhost:" + port + "/api/sessions/1", HttpMethod.GET, entity1, Session.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Session session = response.getBody();

        Reservation reservation = new Reservation();
        reservation.setPrice(10);
        reservation.setSeatNumbers(List.of("A6"));
        reservation.setSession(session);

        HttpEntity<Reservation> entity = new HttpEntity<>(reservation, headers);

        ResponseEntity<Reservation> response1 = restTemplate.exchange("http://localhost:" + port + "/api/reservations", HttpMethod.POST, entity, Reservation.class);

        assertEquals(HttpStatus.CREATED, response1.getStatusCode());

        Reservation savedReservation = response1.getBody();

        System.out.println("sadfghj"+savedReservation);

        assertEquals(10, savedReservation.getPrice());
        assertEquals(1, savedReservation.getSeatNumbers().size());
    }


}