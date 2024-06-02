package deti.tqs.cinemax.controllers;

import deti.tqs.cinemax.config.CustomUserDetailsService;
import deti.tqs.cinemax.config.IAuthenticationFacade;
import deti.tqs.cinemax.config.JwtUtilService;
import deti.tqs.cinemax.models.Movie;
import deti.tqs.cinemax.models.Room;
import deti.tqs.cinemax.models.Session;
import deti.tqs.cinemax.services.SessionService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionController.class)
@AutoConfigureMockMvc(addFilters = false)
class SessionControllerTest {

            @Autowired
            private MockMvc mvc;

            @MockBean
            private CustomUserDetailsService customUserDetailsService;

            @MockBean
            private JwtUtilService jwtUtil;

            @MockBean
            private IAuthenticationFacade authenticationFacade;

            @MockBean
            private SessionService sessionService;


            private ObjectMapper objectMapper = new ObjectMapper();

            @Test
            void testGetAllSessions() throws Exception{
                Movie movie = new Movie();
                movie.setId(1L);
                movie.setTitle("The Avengers");
                movie.setDuration("120");

                Room room = new Room();
                room.setId(1L);
                room.setName("Room 1");
                room.setCapacity(100);
                room.setType("Normal");
                room.setSessions(null);

                Session session = new Session();
                session.setId(1L);
                session.setDate("2021-05-05");
                session.setTime("20:00");
                session.setMovie(movie);
                session.setReservation(null);
                session.setRoom(room);
                session.setBookedSeats(List.of("A1", "A2"));

                Session session1 = new Session();
                session1.setId(2L);
                session1.setDate("2021-05-06");
                session1.setTime("22:45");
                session1.setMovie(movie);
                session1.setReservation(null);
                session1.setRoom(room);
                session1.setBookedSeats(List.of("A1", "A2","A3","A4"));

                List<Session> allSessions = List.of(session, session1);

                Mockito.when(sessionService.getAllSessions()).thenReturn(allSessions);

                mvc.perform(get("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(2)))
                        .andExpect(jsonPath("$[0].date", is("2021-05-05")))
                        .andExpect(jsonPath("$[0].time", is("20:00")))
                        .andExpect(jsonPath("$[1].date", is("2021-05-06")))
                        .andExpect(jsonPath("$[1].time", is("22:45")));
            }

            @Test
            void testGetSessionById() throws Exception{
               Movie movie = new Movie();
               movie.setId(1L);
               movie.setTitle("The Avengers");
               movie.setDuration("120");

               Room room = new Room();
                room.setId(1L);
               room.setName("Room 1");
               room.setCapacity(100);
               room.setType("Normal");
               room.setSessions(null);

               Session session = new Session();
               session.setId(1L);
               session.setDate("2021-05-05");
               session.setTime("20:00");
               session.setMovie(movie);
               session.setReservation(null);
               session.setRoom(room);
               session.setBookedSeats(List.of("A1", "A2"));

               Mockito.when(sessionService.getSessionById(1L)).thenReturn(session);

               mvc.perform(get("/api/sessions/1")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.date", is("2021-05-05")))
                        .andExpect(jsonPath("$.time", is("20:00")));
            }

            @Test
            void tesGetSessionByIdNotFound() throws Exception{
                Mockito.when(sessionService.getSessionById(1L)).thenReturn(null);

                mvc.perform(get("/api/sessions/1")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }

            @Test
            void testSaveSeat() throws Exception{
                Movie movie = new Movie();
                movie.setId(1L);
                movie.setTitle("The Avengers");
                movie.setDuration("120");

                Room room = new Room();
                room.setId(1L);
                room.setName("Room 1");
                room.setCapacity(100);
                room.setType("Normal");
                room.setSessions(null);

                Session session = new Session();
                session.setId(1L);
                session.setDate("2021-05-05");
                session.setTime("20:00");
                session.setMovie(movie);
                session.setReservation(null);
                session.setRoom(room);
                session.setBookedSeats(List.of("A1", "A2"));

                Mockito.when(sessionService.saveSession(Mockito.any())).thenReturn(session);

                mvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(session)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.date", is("2021-05-05")))
                        .andExpect(jsonPath("$.time", is("20:00")))
                        .andExpect(jsonPath("$.bookedSeats", hasSize(2)))
                        .andExpect(jsonPath("$.movie.title", is("The Avengers")));
            }

}
