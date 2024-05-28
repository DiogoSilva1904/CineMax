package deti.tqs.cinemax.services;

import java.time.LocalDateTime;
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
        /*if(!sessionRepository.findByDateAndTimeAndRoom(session.getDate(), session.getTime(), session.getRoom()).isEmpty()) {
            log.info("Session with date {} and time {} and room {} already exists", session.getDate(), session.getTime(), session.getRoom().getId());
            //throw new IllegalArgumentException("Session with date " + session.getDate() + " and time " + session.getTime() + " and room id " + session.getRoom().getId() + " already exists");
            return null;
        }
        else{
            log.info("Saving session with id {}", session.getId());*/
        return sessionRepository.save(session);
        //log.info("Saving session with id {}", session.getId());
        //return sessionRepository.save(session);
    }

    public Session getSessionById(Long id) {
        log.info("Retrieving session with id {}", id);
        return sessionRepository.findById(id).orElse(null);
    }

    public List<Session> getAllSessions() {
        log.info("Retrieving all sessions");
        return sessionRepository.findAll();
    }


    public List<Session> getSessionsbyDate(String date) {
        log.info("Retrieving all sessions by date {}", date);
        List<Session> sessions = new ArrayList<>(sessionRepository.findByDate(date)); // Convert to mutable list
        //checking if the date hour is in the past
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
