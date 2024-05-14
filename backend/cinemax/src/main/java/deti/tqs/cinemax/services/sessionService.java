package deti.tqs.cinemax.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory;

import deti.tqs.cinemax.models.Session;
import deti.tqs.cinemax.repositories.sessionRepository;

@Service
public class sessionService {

    private final sessionRepository sessionRepository;

    private static final Logger log = LoggerFactory.getLogger(sessionService.class);

    public sessionService(sessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void deleteSession(Long id) {
        log.info("Deleting session with id {}", id);
        sessionRepository.deleteById(id);
    }

    public Optional<Session> updateSession(Long id, Session session) {
        Optional<Session> sessionOptional = sessionRepository.findById(id);
        if (sessionOptional.isPresent()) {
            log.info("Updating session with id {}", id);
            session.setId(id);
            return Optional.of(sessionRepository.save(session));
        }
        else {
            log.info("Session with id {} not found", id);
        }

        return sessionOptional;
    }

    public Session saveSession(Session session) {
        log.info("Saving session with id {}", session.getId());
        return sessionRepository.save(session);
    }

    public Session getSessionById(Long id) {
        log.info("Retrieving session with id {}", id);
        return sessionRepository.findById(id).orElse(null);
    }

    public List<Session> getAllSessions() {
        log.info("Retrieving all sessions");
        return sessionRepository.findAll();
    }

}
