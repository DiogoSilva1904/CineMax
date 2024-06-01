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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
        user.setUsername("user1");
        Reservation reservation = new Reservation();
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
        user.setUsername("user1");
        Reservation reservation = new Reservation();
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




}
