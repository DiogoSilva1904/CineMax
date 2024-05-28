package deti.tqs.cinemax.repositories;

import deti.tqs.cinemax.models.Room;
import deti.tqs.cinemax.models.Session;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import jakarta.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class SessionRepositotyTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private SessionRepository sessionRepository;

    @Test
    void testFindByDateTimeAndRoom() {
        Room room = new Room();
        room.setName("Room 1");
        entityManager.persist(room);
        entityManager.flush();

        Session session = new Session();
        session.setDate("2021-01-01");
        session.setTime("20:00");
        session.setRoom(room);
        sessionRepository.save(session);

        // when
        List<Session> found = sessionRepository.findByDateAndTimeAndRoom("2021-01-01", "20:00", room);

        // then
        assertThat(found).isNotEmpty();
        assertThat(found).contains(session);
    }
}

