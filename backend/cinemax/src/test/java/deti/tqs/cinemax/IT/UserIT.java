package deti.tqs.cinemax.IT;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import deti.tqs.cinemax.models.AppUser;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.profiles.active=test"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserIT {

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
    void whenGetUserById_thenReturnUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<AppUser> response = restTemplate.exchange("http://localhost:" + port + "/api/users/1", HttpMethod.GET, entity, AppUser.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        AppUser user = response.getBody();
        assertEquals("admin", user.getUsername());
        assertEquals("ADMIN", user.getRole());
        assertEquals("admin@gmail.com", user.getEmail());
    }

    @Test
    @Order(2)
    void whenGetUserById_thenReturnNotFound() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<AppUser> response = restTemplate.exchange("http://localhost:" + port + "/api/users/10", HttpMethod.GET, entity, AppUser.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(3)
    @Disabled("tirar disabled quando a branch 23 for para a dev")
    void whenDeleteUser_thenReturnNoContent(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + port + "/api/users/2", HttpMethod.DELETE, entity, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Order(4)
    @Disabled("tirar disabled quando a branch 23 for para a dev")
    void whenDeleteUser_ReturnNotFound(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + port + "/api/users/10", HttpMethod.DELETE, entity, Void.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
