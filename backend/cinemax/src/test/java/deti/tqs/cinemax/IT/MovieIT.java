package deti.tqs.cinemax.IT;

import deti.tqs.cinemax.models.Movie;
import org.apache.coyote.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Test
    void testGetAllMovies() {
        ResponseEntity<List<Movie>> response = restTemplate.exchange("http://localhost:" + port + "/api/movies", HttpMethod.GET ,null,new ParameterizedTypeReference<List<Movie>>() {});

        List<Movie> movies = response.getBody();
        assertEquals(3, movies.size());
    }
}
