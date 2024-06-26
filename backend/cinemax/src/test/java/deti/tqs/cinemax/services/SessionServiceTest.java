package deti.tqs.cinemax.services;

import deti.tqs.cinemax.models.Movie;
import deti.tqs.cinemax.models.Room;
import deti.tqs.cinemax.models.Session;
import deti.tqs.cinemax.repositories.SessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class SessionServiceTest {
    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private SessionService sessionService;


    @Test
    void testGetSessionById_Found() {
        long sessionId = 1L;
        Session expectedSession = new Session();
        expectedSession.setId(sessionId);

        log.info("Mocking sessionRepository.findById({}) to return a session", sessionId);
        Mockito.when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(expectedSession));

        Session retrievedSession = sessionService.getSessionById(sessionId);

        assertNotNull(retrievedSession);
        assertEquals(sessionId, retrievedSession.getId());
        log.info("Retrieved session with id {}", sessionId);
    }

    @Test
    void testGetSessionById_NotFound() {
        long sessionId = 1L;

        log.info("Mocking sessionRepository.findById({}) to return empty", sessionId);
        Mockito.when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        Session retrievedSession = sessionService.getSessionById(sessionId);

        assertNull(retrievedSession);
        log.info("Session with id {} not found", sessionId);
    }

    @Test
    void testSaveSession() {
        Session newSession = new Session();
        newSession.setId(1L);

        Room room = new Room();
        room.setId(1L);
        room.setCapacity(100);

        newSession.setRoom(room);

        log.info("Calling sessionService.saveSession(session={})", newSession);
        Mockito.when(sessionRepository.save(newSession)).thenReturn(newSession);


        Session savedSession = sessionService.saveSession(newSession);

        assertNotNull(savedSession);
        assertEquals(newSession.getId(), savedSession.getId());
        log.info("Saved session: {}", savedSession);
    }

    @Test
    void testUpdateSession_Found() {
        Long id = 1L;
        Session existingSession = new Session(id, "2024-05-11", "20:00", null,null, null, new ArrayList<>());
        Session updatedSession = new Session(id, "2024-05-11", "21:00", null, null, null, new ArrayList<>());

        log.info("Mocking sessionRepository.findById({}) to return existing session", id);
        Mockito.when(sessionRepository.findById(id)).thenReturn(Optional.of(existingSession));
        log.info("Mocking sessionRepository.save(session={}) to return updated session", updatedSession);
        Mockito.when(sessionRepository.save(updatedSession)).thenReturn(updatedSession);

        Optional<Session> result = sessionService.updateSession(id, updatedSession);

        assertTrue(result.isPresent());
        assertEquals(updatedSession, result.get());

        verify(sessionRepository, times(1)).findById(id);
        verify(sessionRepository, times(1)).save(updatedSession);
    }

    @Test
    void testUpdateSession_NotFound() {
        long sessionId = 1L;

        log.info("Mocking sessionRepository.findById({}) to return empty", sessionId);
        Mockito.when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        Session updatedSession = new Session();
        updatedSession.setId(sessionId);

        Optional<Session> updatedOptionalSession = sessionService.updateSession(sessionId, updatedSession);

        assertTrue(updatedOptionalSession.isEmpty());
        log.info("Session with id {} not found for update", sessionId);
    }

    @Test
    void testDeleteSession() {
        long sessionId = 1L;

        log.info("Calling sessionService.deleteSession(id={})", sessionId);

        sessionService.deleteSession(sessionId);

        verify(sessionRepository, times(1)).deleteById(sessionId);
    }

    @Test
    void testGetAllSessions(){
        Session session = new Session(1L, "2024-05-11", "20:00", null,null, null, new ArrayList<>());
        Session session2 = new Session(2L, "2024-05-11", "21:00", null,null, null, new ArrayList<>());

        log.info("Mocking sessionRepository.findAll() to return a list of sessions");

        Mockito.when(sessionRepository.findAll()).thenReturn(java.util.List.of(session, session2));

        log.info("Calling sessionService.getAllSessions()");

        List<Session> sessionList = sessionService.getAllSessions();

        assertEquals(2, sessionList.size());
        assertEquals(session, sessionList.get(0));
        assertEquals(session2, sessionList.get(1));
    }

    @Test
    //only gets session in the future
    void testGetSessionsByDate(){
        Session session2 = new Session(2L, "2024-06-11", "21:00", null,null, null, new ArrayList<>());

        log.info("Mocking sessionRepository.findAllByDate('2024-05-11') to return a list of sessions");

        Mockito.when(sessionRepository.findByDate("2024-06-11")).thenReturn(List.of(session2));

        log.info("Calling sessionService.getSessionsByDate('2024-06-11')");

        List<Session> sessionList = sessionService.getSessionsbyDate("2024-06-11");

        assertEquals(1, sessionList.size());
        assertEquals(session2, sessionList.get(0));

    }

    @Test
    void testSaveSessionOverlapping() {
        Room room = new Room();
        room.setId(1L);
        room.setCapacity(100);

        Movie movie = new Movie();
        movie.setId(1L);
        movie.setDuration("120");

        Session session1 = new Session();
        session1.setId(1L);
        session1.setDate("2024-05-11");
        session1.setTime("20:00");
        session1.setMovie(movie);
        session1.setRoom(room);

        Mockito.when(sessionRepository.findByDate("2024-05-11")).thenReturn(List.of(session1));
        Mockito.when(sessionRepository.save(any(Session.class))).thenReturn(session1);


        Session session2 = new Session();
        session2.setId(2L);
        session2.setDate("2024-05-11");
        session2.setTime("21:00");
        session2.setMovie(movie);
        session2.setRoom(room);

        log.info("Calling sessionService.saveSession(session={})", session2);

        Session savedSession1 = sessionRepository.save(session1);

        assertEquals(session1, savedSession1);

        Session savedSession2 = sessionService.saveSession(session2);

        assertNull(savedSession2);

        log.info("Session overlaps with another session");

    }

        

}
