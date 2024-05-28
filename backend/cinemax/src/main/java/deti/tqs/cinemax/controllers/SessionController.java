package deti.tqs.cinemax.controllers;

import deti.tqs.cinemax.models.Session;
import deti.tqs.cinemax.services.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "localhost:4200")
@RequestMapping("/api/sessions")
@Tag(name = "session", description = "Endpoints to manage session")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Operation(summary = "Get all sessions")
    @GetMapping
    public ResponseEntity<List<Session>> getAllSessions() {
        List<Session> sessions = sessionService.getAllSessions();
        return new ResponseEntity<>(sessions, HttpStatus.OK);
    }

    @Operation(summary = "Get sessions by id")
    @GetMapping("{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable Long id) {
        Session session = sessionService.getSessionById(id);
        if (session == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(session, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Session> saveSession(@RequestBody Session session) {
        Session updatedSession = sessionService.saveSession(session);
        return new ResponseEntity<>(updatedSession, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Session>> getSessionsByDate(@PathVariable String date) {
        List<Session> sessions = sessionService.getSessionsbyDate(date);
        return new ResponseEntity<>(sessions, HttpStatus.OK);
    }
}
