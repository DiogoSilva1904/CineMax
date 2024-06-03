package deti.tqs.cinemax.services;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory;

import deti.tqs.cinemax.models.Session;
import deti.tqs.cinemax.repositories.SessionRepository;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    private static final Logger log = LoggerFactory.getLogger(SessionService.class);

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    /**
     * Deletes a session by its ID.
     *
     * @param id the ID of the session to delete.
     */
    public void deleteSession(Long id) {
        log.info("Deleting session with id {}", id);
        sessionRepository.deleteById(id);
    }

    /**
     * Updates an existing session.
     *
     * @param id      the ID of the session to update.
     * @param session the updated session details.
     * @return an Optional containing the updated session if successful, or an empty Optional if the session was not found.
     */
    public Optional<Session> updateSession(Long id, Session session) {
        Optional<Session> sessionOptional = sessionRepository.findById(id);
        if (sessionOptional.isPresent()) {
            log.info("Updating session with id {}", id);
            session.setId(id);
            return Optional.of(sessionRepository.save(session));
        } else {
            log.info("Session with id {} not found", id);
        }

        return sessionOptional;
    }

    /**
     * Saves a session to the repository.
     * Ensures there are no overlapping sessions in the same room.
     *
     * @param session the session to save.
     * @return the saved session, or null if there is an overlapping session.
     */
    public Session saveSession(Session session) {
        List<Session> sessions = sessionRepository.findByDate(session.getDate());
        for (Session s : sessions) {
            if (isOverlapping(s, session) && s.getRoom().getId().equals(session.getRoom().getId())) {
                log.info("Session overlaps with another session");
                return null;
            }
        }

        return sessionRepository.save(session);
    }

    /**
     * Checks if two sessions are overlapping.
     *
     * @param s1 the first session.
     * @param s2 the second session.
     * @return true if the sessions are overlapping, false otherwise.
     */
    private boolean isOverlapping(Session s1, Session s2) {
        LocalTime s1StartTime = LocalTime.parse(s1.getTime());
        LocalTime s1EndTime = s1StartTime.plusMinutes(Long.parseLong(s1.getMovie().getDuration()));

        LocalTime s2StartTime = LocalTime.parse(s2.getTime());
        LocalTime s2EndTime = s2StartTime.plusMinutes(Long.parseLong(s2.getMovie().getDuration()));

        return s1StartTime.isBefore(s2EndTime) && s2StartTime.isBefore(s1EndTime);
    }

    /**
     * Retrieves a session by its ID.
     *
     * @param id the ID of the session.
     * @return the session with the specified ID, or null if not found.
     */
    public Session getSessionById(Long id) {
        log.info("Retrieving session with id {}", id);
        return sessionRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves all sessions from the repository.
     *
     * @return a list of all sessions.
     */
    public List<Session> getAllSessions() {
        log.info("Retrieving all sessions");
        return sessionRepository.findAll();
    }

    /**
     * Retrieves all sessions for a specific date, excluding those that have already passed.
     *
     * @param date the date to filter sessions by.
     * @return a list of sessions on the specified date that have not yet passed.
     */
    public List<Session> getSessionsbyDate(String date) {
        log.info("Retrieving all sessions by date {}", date);
        List<Session> sessions = new ArrayList<>(sessionRepository.findByDate(date)); // Convert to mutable list

        List<Session> sessionsToRemove = new ArrayList<>();

        for (Session session : sessions) {
            String sessionDate = session.getDate() + " " + session.getTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(sessionDate, formatter);
            if (LocalDateTime.now().isAfter(dateTime)) {
                sessionsToRemove.add(session);
            }
        }

        sessions.removeAll(sessionsToRemove);

        return sessions;
    }

}
