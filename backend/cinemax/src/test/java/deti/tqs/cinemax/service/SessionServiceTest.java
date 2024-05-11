package deti.tqs.cinemax.service;

import deti.tqs.cinemax.models.session;
import deti.tqs.cinemax.repositories.sessionRepository;
import deti.tqs.cinemax.services.sessionService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@Slf4j
class SessionServiceTest {
    @Mock
    private sessionRepository sessionRepository;

    @InjectMocks
    private sessionService sessionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetSessionById_Found() {
        long sessionId = 1L;
        session expectedSession = new session();
        expectedSession.setId(sessionId);

        log.info("Mocking sessionRepository.findById({}) to return a session", sessionId);
        Mockito.when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(expectedSession));

        session retrievedSession = sessionService.getSessionById(sessionId);

        assertNotNull(retrievedSession);
        assertEquals(sessionId, retrievedSession.getId());
        log.info("Retrieved session with id {}", sessionId);
    }

    @Test
    void testGetSessionById_NotFound() {
        long sessionId = 1L;

        log.info("Mocking sessionRepository.findById({}) to return empty", sessionId);
        Mockito.when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        session retrievedSession = sessionService.getSessionById(sessionId);

        assertNull(retrievedSession);
        log.info("Session with id {} not found", sessionId);
    }

    @Test
    void testSaveSession() {
        session newSession = new session();
        newSession.setId(1L);

        log.info("Calling sessionService.saveSession(session={})", newSession);
        Mockito.when(sessionRepository.save(newSession)).thenReturn(newSession);

        session savedSession = sessionService.saveSession(newSession);

        assertNotNull(savedSession);
        assertEquals(newSession.getId(), savedSession.getId());
        log.info("Saved session: {}", savedSession);
    }

    @Test
    void testUpdateSession_Found() {
        Long id = 1L;
        session existingSession = new session(id, "2024-05-11", "20:00", null, null, null, new ArrayList<>());
        session updatedSession = new session(id, "2024-05-11", "21:00", null, null, null, new ArrayList<>());

        log.info("Mocking sessionRepository.findById({}) to return existing session", id);
        Mockito.when(sessionRepository.findById(id)).thenReturn(Optional.of(existingSession));
        log.info("Mocking sessionRepository.save(session={}) to return updated session", updatedSession);
        Mockito.when(sessionRepository.save(updatedSession)).thenReturn(updatedSession);

        Optional<session> result = sessionService.updateSession(id, updatedSession);

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

        session updatedSession = new session();
        updatedSession.setId(sessionId);

        Optional<session> updatedOptionalSession = sessionService.updateSession(sessionId, updatedSession);

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
}
