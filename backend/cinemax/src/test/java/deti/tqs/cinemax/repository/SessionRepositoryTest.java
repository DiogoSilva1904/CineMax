package deti.tqs.cinemax.repositories;

import deti.tqs.cinemax.models.Movie;
import deti.tqs.cinemax.models.Room;
import deti.tqs.cinemax.models.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
class SessionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SessionRepository sessionRepository;

    private Movie movie1;
    private Room room1;
    private Session session1;
    private Session session2;

    @BeforeEach
    public void setUp() {
        movie1 = new Movie();
        movie1.setTitle("Movie 1");
        movie1.setCategory("Category 1");
        movie1.setGenre("Genre 1");
        movie1.setStudio("Studio 1");
        movie1.setDuration("120 min");
        movie1.setImagePath("path/to/image1");
        entityManager.persist(movie1);

        room1 = new Room();
        room1.setName("Room 1");
        room1.setCapacity(100);
        room1.setType("Type 1");
        entityManager.persist(room1);

        session1 = new Session();
        session1.setDate("2024-06-01");
        session1.setTime("10:00");
        session1.setMovie(movie1);
        session1.setRoom(room1);
        session1.setBookedSeats(List.of("A1", "A2"));

        session2 = new Session();
        session2.setDate("2024-06-01");
        session2.setTime("12:00");
        session2.setMovie(movie1);
        session2.setRoom(room1);
        session2.setBookedSeats(List.of("B1", "B2"));

        entityManager.persist(session1);
        entityManager.persist(session2);
        entityManager.flush();
    }

    @Test
    void testFindByDate() {
        List<Session> sessions = sessionRepository.findByDate("2024-06-01");
        assertThat(sessions).hasSize(2).extracting(Session::getMovie).extracting(Movie::getTitle)
                .containsExactlyInAnyOrder("Movie 1", "Movie 1");
    }

    @Test
    void testSaveSession() {
        Session session = new Session();
        session.setDate("2024-06-02");
        session.setTime("14:00");
        session.setMovie(movie1);
        session.setRoom(room1);
        session.setBookedSeats(List.of("C1", "C2"));

        Session savedSession = sessionRepository.save(session);
        assertThat(savedSession).isNotNull();
        assertThat(savedSession.getId()).isNotNull();
    }

    @Test
    void testDeleteSession() {
        sessionRepository.deleteById(session1.getId());
        assertThat(sessionRepository.findById(session1.getId())).isEmpty();
    }

    @Transactional
    @Test
    void testUpdateSession() {
        session1.setMovie(movie1);
        session1.setRoom(room1);
        session1.setTime("14:00");
        entityManager.persistAndFlush(session1);
        Session retrievedSession = sessionRepository.findById(session1.getId()).orElse(null);
        assertThat(retrievedSession).isNotNull();
        assertThat(retrievedSession.getTime()).isEqualTo("14:00");
    }


    @Test
    void testFindById() {
        Session foundSession = sessionRepository.findById(session1.getId()).orElse(null);
        assertThat(foundSession).isNotNull();
        assertThat(foundSession.getMovie().getTitle()).isEqualTo("Movie 1");
        assertThat(foundSession.getRoom().getName()).isEqualTo("Room 1");
    }
}
