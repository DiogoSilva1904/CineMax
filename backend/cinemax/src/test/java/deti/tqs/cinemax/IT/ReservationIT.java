package deti.tqs.cinemax.IT;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import deti.tqs.cinemax.models.AppUser;
import deti.tqs.cinemax.models.Reservation;
import deti.tqs.cinemax.models.Session;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        assertEquals(HttpStatus.OK, response.getStatusCode());

        String responseBody = response.getBody();
        System.out.println("asdfgh"+responseBody);

        JsonElement jsonElement = JsonParser.parseString(responseBody);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        jwtToken = jsonObject.get("jwt").getAsString();
    }

    @Test
    @Order(1)
    void whenGetAllReservations_ReturnAllReservations(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<List<Reservation>> response = restTemplate.exchange("http://localhost:" + port + "/api/reservations", HttpMethod.GET, entity, new ParameterizedTypeReference<List<Reservation>>() {} );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Reservation> reservations = response.getBody();

        assertEquals(3, reservations.size());
        assertEquals("user", reservations.get(0).getUser().getUsername());
        assertEquals(1,reservations.get(0).getSeatNumbers().size());
        assertEquals("user", reservations.get(1).getUser().getUsername());
        assertEquals(2,reservations.get(1).getSeatNumbers().size());
        assertEquals("admin", reservations.get(2).getUser().getUsername());
        assertEquals(1,reservations.get(2).getSeatNumbers().size());

    }

    @Test
    @Order(2)
    void whenGetReservationById_ReturnReservation() {
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
    void whenGetReservationsByUser_ReturnReservations() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<List<Reservation>> response = restTemplate.exchange("http://localhost:" + port + "/api/reservations/user/user", HttpMethod.GET, entity, new ParameterizedTypeReference<List<Reservation>>() {} );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Reservation> reservations = response.getBody();

        assertEquals(2, reservations.size());
        assertEquals("user", reservations.get(0).getUser().getUsername());
        assertEquals(1,reservations.get(0).getSeatNumbers().size());
        assertEquals("user", reservations.get(1).getUser().getUsername());
        assertEquals(2,reservations.get(1).getSeatNumbers().size());
    }

    @Test
    @Order(4)
    void whenGetReservationById_ReturnNotFound() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Reservation> response = restTemplate.exchange("http://localhost:" + port + "/api/reservations/100", HttpMethod.GET, entity, Reservation.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(5)
    void whenReservationDone_ValidateTicket(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Reservation> response = restTemplate.exchange("http://localhost:" + port + "/api/reservations/1", HttpMethod.PUT, entity, Reservation.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Reservation reservation = response.getBody();

        assertTrue(reservation.isUsed());
        assertEquals(1, reservation.getId());
    }

    @Test
    @Order(6)//this test can only be runned after the tests above(whenReservationDone_ValidateTicket) is runned first
    void whenReservationDone_TicketAlreadyValidated(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Reservation> response = restTemplate.exchange("http://localhost:" + port + "/api/reservations/1", HttpMethod.PUT, entity, Reservation.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(7)
    void whenCreateReservation_ReturnReservation() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        HttpEntity<?> entity1 = new HttpEntity<>(headers);

        ResponseEntity<Session> response = restTemplate.exchange("http://localhost:" + port + "/api/sessions/1", HttpMethod.GET, entity1, Session.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Session session = response.getBody();

        ResponseEntity<AppUser> response2 = restTemplate.exchange("http://localhost:" + port + "/api/users/2", HttpMethod.GET, entity1, AppUser.class);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        AppUser user = response2.getBody();

        Reservation reservation = new Reservation();
        reservation.setPrice(10);
        reservation.setSeatNumbers(List.of("A6"));
        reservation.setSession(session);
        reservation.setUser(user);

        HttpEntity<Reservation> entity = new HttpEntity<>(reservation, headers);

        ResponseEntity<Reservation> response1 = restTemplate.exchange("http://localhost:" + port + "/api/reservations", HttpMethod.POST, entity, Reservation.class);

        assertEquals(HttpStatus.CREATED, response1.getStatusCode());

        Reservation savedReservation = response1.getBody();

        System.out.println("sadfghj"+savedReservation);

        assertEquals(10, savedReservation.getPrice());
        assertEquals(1, savedReservation.getSeatNumbers().size());
    }

    @Test
    @Order(8)
    void whenCreateReservation_SeatAlreadyBooked() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        HttpEntity<?> entity1 = new HttpEntity<>(headers);

        ResponseEntity<Session> response = restTemplate.exchange("http://localhost:" + port + "/api/sessions/1", HttpMethod.GET, entity1, Session.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Session session = response.getBody();

        ResponseEntity<AppUser> response2 = restTemplate.exchange("http://localhost:" + port + "/api/users/2", HttpMethod.GET, entity1, AppUser.class);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        AppUser user = response2.getBody();

        Reservation reservation = new Reservation();
        reservation.setPrice(10);
        reservation.setSeatNumbers(List.of("A1"));
        reservation.setSession(session);
        reservation.setUser(user);

        HttpEntity<Reservation> entity = new HttpEntity<>(reservation, headers);

        ResponseEntity<Reservation> response1 = restTemplate.exchange("http://localhost:" + port + "/api/reservations", HttpMethod.POST, entity, Reservation.class);

        assertEquals(HttpStatus.CONFLICT, response1.getStatusCode());
    }
}