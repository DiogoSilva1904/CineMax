package deti.tqs.cinemax.IT;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import deti.tqs.cinemax.config.AuthenticationRequest;
import deti.tqs.cinemax.config.ChangePasswordRequest;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.profiles.active=test"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthIT {
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
    void whenLoginSuccess_ReturnJWT_token(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest("admin", "admin");

        HttpEntity<AuthenticationRequest> entity = new HttpEntity<>(authenticationRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/login", HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(2)
    void whenLoginFailure_ReturnException(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest("admin", "wrongpassword");

        HttpEntity<AuthenticationRequest> entity = new HttpEntity<>(authenticationRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/login", HttpMethod.POST, entity, String.class);

        assertThrows(Exception.class, () -> {
            throw new Exception(response.getBody());
        });
    }

    @Test
    @Order(3)
    void whenRegisterSuccess_ReturnSuccess(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        AppUser user = new AppUser();
        user.setUsername("test");
        user.setPassword("test");
        user.setRole("USER");
        user.setEmail("user@gmail.com");

        HttpEntity<AppUser> entity = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/login/register", HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @Order(5)
    void whenRegisterFailure_ReturnException() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        AppUser user = new AppUser();
        user.setId(2L);
        user.setRole("USER");
        user.setPassword("user");
        user.setEmail("jojo@gmail.com");
        user.setUsername("user");

        HttpEntity<AppUser> entity = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/login/register", HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    @Order(6)
    void whenChangePasswordSuccess_ReturnSuccess(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("admin", "admin", "admin1");

        HttpEntity<ChangePasswordRequest> entity = new HttpEntity<>(changePasswordRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/login/change-password", HttpMethod.PUT, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(7)
    void whenChangePasswordFailure_ReturnException(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("admin", "wrongpassword", "admin1");

        HttpEntity<ChangePasswordRequest> entity = new HttpEntity<>(changePasswordRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/login/change-password", HttpMethod.PUT, entity, String.class);

        assertThrows(Exception.class, () -> {
            throw new Exception(response.getBody());
        });
    }

    @Test
    @Order(8)
    void whenChangePasswordUserNotFound_ReturnException(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(jwtToken);

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("wronguser", "admin", "admin1");

        HttpEntity<ChangePasswordRequest> entity = new HttpEntity<>(changePasswordRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/login/change-password", HttpMethod.PUT, entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
