package deti.tqs.cinemax.IT;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import deti.tqs.cinemax.models.Room;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.profiles.active=test"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoomIT {
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
    void whenGetAllRooms_ReturnAllRooms() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<List<Room>> response = restTemplate.exchange("http://localhost:" + port + "/api/rooms", HttpMethod.GET, entity, new ParameterizedTypeReference<List<Room>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Room> rooms = response.getBody();

        assertEquals(3, rooms.size());
        assertEquals("Room A", rooms.get(0).getName());
        assertEquals(50, rooms.get(0).getCapacity());
        assertEquals("Standard", rooms.get(0).getType());
        assertEquals("Room B", rooms.get(1).getName());
        assertEquals(30, rooms.get(1).getCapacity());
        assertEquals("Premium", rooms.get(1).getType());
        assertEquals("Room C", rooms.get(2).getName());
        assertEquals(100, rooms.get(2).getCapacity());
        assertEquals("Standard", rooms.get(2).getType());

    }

    @Test
    @Order(2)
    void whenGetById_ReturnRoom() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Room> response = restTemplate.exchange("http://localhost:" + port + "/api/rooms/1", HttpMethod.GET, entity, Room.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Room room = response.getBody();

        assertEquals("Room A", room.getName());
        assertEquals(50, room.getCapacity());
        assertEquals("Standard", room.getType());
    }

    @Test
    @Order(3)
    void whenGetById_ReturnNotFound() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Room> response = restTemplate.exchange("http://localhost:" + port + "/api/rooms/4", HttpMethod.GET, entity, Room.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(4)
    void whenCreateRoom_ReturnCorrectResponse() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        Room room = new Room();
        room.setName("Room D");
        room.setCapacity(200);
        room.setType("IMAX");

        HttpEntity<Room> entity = new HttpEntity<>(room, headers);

        ResponseEntity<Room> response = restTemplate.exchange("http://localhost:" + port + "/api/rooms", HttpMethod.POST, entity, Room.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Room savedRoom = response.getBody();

        assertEquals("Room D", savedRoom.getName());
        assertEquals(200, savedRoom.getCapacity());
        assertEquals("IMAX", savedRoom.getType());
    }


}
