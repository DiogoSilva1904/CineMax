package deti.tqs.cinemax.service;

import deti.tqs.cinemax.models.session;
import deti.tqs.cinemax.repositories.sessionRepository;
import deti.tqs.cinemax.services.sessionService;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class SessionServiceTest {
    @Mock
    private sessionRepository sessionRepository;

    @InjectMocks
    private sessionService sessionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private static final Logger log = LoggerFactory.getLogger(SessionServiceTest.class);


    @Test
    void testGetSessionById() {
        // Given
        long sessionId = 1L;
        session session = new session();
        session.setId(sessionId);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // When
        session retrievedSession = sessionService.getSessionById(sessionId);

        // Then
        Assertions.assertNotNull(retrievedSession);
        Assertions.assertEquals(sessionId, retrievedSession.getId());
    }

    @Test
    void testGetSessionByIdNotFound() {
        // Given
        long sessionId = 1L;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // When
        session retrievedSession = sessionService.getSessionById(sessionId);

        // Then
        Assertions.assertNull(retrievedSession);
    }

    @Test
    void testSaveSession() {
        // Given
        session session = new session();
        session.setId(1L);
        when(sessionRepository.save(session)).thenReturn(session);

        // When
        session savedSession = sessionService.saveSession(session);

        // Then
        Assertions.assertNotNull(savedSession);
        Assertions.assertEquals(session.getId(), savedSession.getId());
    }

    @Test
    void testUpdateSession() {
        // Arrange
        Long id = 1L;
        session existingSession = new session(id, "2024-05-11", "20:00", null, null, null, new ArrayList<>());
        session updatedSession = new session(id, "2024-05-11", "21:00", null, null, null, new ArrayList<>());

        when(sessionRepository.findById(id)).thenReturn(Optional.of(existingSession));
        when(sessionRepository.save(updatedSession)).thenReturn(updatedSession);

        // Act
        Optional<session> result = sessionService.updateSession(id, updatedSession);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(updatedSession, result.get());

        verify(sessionRepository, times(1)).findById(id);
        verify(sessionRepository, times(1)).save(updatedSession);
    }

    private void assertEquals(session updatedSession, session session) {

    }


    @Test
    void testUpdateSessionNotFound() {
        // Given
        long sessionId = 1L;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        session updatedSession = new session();
        updatedSession.setId(sessionId);

        // When
        Optional<session> updatedOptionalSession = sessionService.updateSession(sessionId, updatedSession);

        // Then
        assertTrue(updatedOptionalSession.isEmpty());
    }

    @Test
    void testDeleteSession() {
        // Given
        long sessionId = 1L;

        // When
        sessionService.deleteSession(sessionId);

        // Then
        verify(sessionRepository, times(1)).deleteById(sessionId);
    }
}
