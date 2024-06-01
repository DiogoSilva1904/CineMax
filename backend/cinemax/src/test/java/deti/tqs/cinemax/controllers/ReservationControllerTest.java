package deti.tqs.cinemax.controllers;

import deti.tqs.cinemax.config.CustomUserDetailsService;
import deti.tqs.cinemax.config.IAuthenticationFacade;
import deti.tqs.cinemax.config.JwtUtilService;
import deti.tqs.cinemax.models.AppUser;
import deti.tqs.cinemax.models.Reservation;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import deti.tqs.cinemax.services.ReservationService;

import java.util.List;


@WebMvcTest(ReservationController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReservationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtUtilService jwtUtil;

    @MockBean
    private IAuthenticationFacade authenticationFacade;

    @MockBean
    private ReservationService reservationService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetReservationById() throws Exception {
        AppUser user = new AppUser();
        user.setId(1L);
        user.setUsername("user1");

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(user);
        reservation.setSession(null);
        reservation.setSeatNumbers(List.of("A1", "A2"));

        Mockito.when(reservationService.getReservationById(1L)).thenReturn(reservation);

        mvc.perform(get("/api/reservations/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.user.username", is("user1")))
            .andExpect(jsonPath("$.seatNumbers", hasSize(2)))
            .andExpect(jsonPath("$.seatNumbers[0]", is("A1")))
            .andExpect(jsonPath("$.seatNumbers[1]", is("A2")));
    }

    @Test
    void testGetReservationByIdNotFound() throws Exception {
        Mockito.when(reservationService.getReservationById(1L)).thenReturn(null);

        mvc.perform(get("/api/reservations/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testSaveReservation() throws Exception {
        AppUser user = new AppUser();
        user.setId(1L);
        user.setUsername("user1");

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(user);
        reservation.setSession(null);
        reservation.setSeatNumbers(List.of("J1"));

        Mockito.when(reservationService.saveReservation(Mockito.any())).thenReturn(reservation);

        mvc.perform(post("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reservation)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.user.username", is("user1")))
            .andExpect(jsonPath("$.seatNumbers", hasSize(1)))
            .andExpect(jsonPath("$.seatNumbers[0]", is("J1")));
    }


    @Test
    void testGetAllReservations() throws Exception{
        AppUser user = new AppUser();
        user.setId(1L);
        user.setUsername("user1");

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(user);
        reservation.setSession(null);
        reservation.setSeatNumbers(List.of("J1"));

        Reservation reservation2 = new Reservation();
        reservation2.setId(2L);
        reservation2.setUser(user);
        reservation2.setSession(null);
        reservation2.setSeatNumbers(List.of("J2"));

        Mockito.when(reservationService.getAllReservations()).thenReturn(List.of(reservation, reservation2));

        mvc.perform(get("/api/reservations"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].user.username", is("user1")))
            .andExpect(jsonPath("$[0].seatNumbers", hasSize(1)))
            .andExpect(jsonPath("$[0].seatNumbers[0]", is("J1")))
            .andExpect(jsonPath("$[1].user.username", is("user1")))
            .andExpect(jsonPath("$[1].seatNumbers", hasSize(1)))
            .andExpect(jsonPath("$[1].seatNumbers[0]", is("J2")));

    }

    @Test
    void testGetReservationsByUser() throws Exception{
        AppUser user = new AppUser();
        user.setId(1L);
        user.setUsername("user1");

        AppUser user2 = new AppUser();
        user2.setId(2L);
        user2.setUsername("user2");

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(user);
        reservation.setSession(null);
        reservation.setSeatNumbers(List.of("J1"));

        Reservation reservation2 = new Reservation();
        reservation2.setId(2L);
        reservation2.setUser(user);
        reservation2.setSession(null);
        reservation2.setSeatNumbers(List.of("J2"));

        Reservation reservation3 = new Reservation();
        reservation3.setId(3L);
        reservation3.setUser(user2);
        reservation3.setSession(null);
        reservation3.setSeatNumbers(List.of("J3"));

        Mockito.when(reservationService.getReservationsByUser(user.getUsername())).thenReturn(List.of(reservation, reservation2));

        mvc.perform(get("/api/reservations/user/user1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].user.username", is("user1")))
            .andExpect(jsonPath("$[0].seatNumbers", hasSize(1)))
            .andExpect(jsonPath("$[0].seatNumbers[0]", is("J1")))
            .andExpect(jsonPath("$[1].user.username", is("user1")))
            .andExpect(jsonPath("$[1].seatNumbers", hasSize(1)))
            .andExpect(jsonPath("$[1].seatNumbers[0]", is("J2")));

    }

    @Test
    void testMakeReservationUsed() throws Exception{
        AppUser user = new AppUser();
        user.setId(1L);
        user.setUsername("user1");

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(user);
        reservation.setSession(null);
        reservation.setUsed(false);
        reservation.setSeatNumbers(List.of("J1"));

        Reservation reservation1 = new Reservation();
        reservation1.setId(1L);
        reservation1.setUser(user);
        reservation1.setSession(null);
        reservation1.setUsed(true);
        reservation1.setSeatNumbers(List.of("J1"));



        Mockito.when(reservationService.makeReservationUsed(1L)).thenReturn(reservation1);

        mvc.perform(put("/api/reservations/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.user.username", is("user1")))
            .andExpect(jsonPath("$.seatNumbers", hasSize(1)))
            .andExpect(jsonPath("$.seatNumbers[0]", is("J1")))
            .andExpect(jsonPath("$.used", is(true)));
    }




}
