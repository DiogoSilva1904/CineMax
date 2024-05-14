package deti.tqs.cinemax.controllers;

import deti.tqs.cinemax.models.Seat;
import deti.tqs.cinemax.services.SeatService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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

@WebMvcTest(SeatController.class)
class SeatControllerTest {

        @Autowired
        private MockMvc mvc;

        @MockBean
        private SeatService seatService;

        private ObjectMapper objectMapper = new ObjectMapper();

        @Test
        void testGetAlllSeats() throws Exception{
            Seat seat = new Seat();
            seat.setSeatIdentifier("A1");
            seat.setPriceMultiplier(1);
            seat.setRoom(null);

            Seat seat1 = new Seat();
            seat1.setSeatIdentifier("G2");
            seat1.setPriceMultiplier(3);
            seat1.setRoom(null);

            List<Seat> allSeats = List.of(seat, seat1);

            Mockito.when(seatService.getAllSeats()).thenReturn(allSeats);

            mvc.perform(get("/api/seats")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].seatIdentifier", is("A1")))
                    .andExpect(jsonPath("$[0].priceMultiplier", is(1)))
                    .andExpect(jsonPath("$[1].seatIdentifier", is("G2")))
                    .andExpect(jsonPath("$[1].priceMultiplier", is(3)));
        }

        @Test
        void testGetSeatById() throws Exception{
            Seat seat = new Seat();
            seat.setSeatIdentifier("A1");
            seat.setPriceMultiplier(1);
            seat.setRoom(null);

            Mockito.when(seatService.getSeatById(1L)).thenReturn(seat);

            mvc.perform(get("/api/seats/1")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.seatIdentifier", is("A1")))
                    .andExpect(jsonPath("$.priceMultiplier", is(1)));
        }

        @Test
        void testGetSeatByIdNotFound() throws Exception{
            Mockito.when(seatService.getSeatById(1L)).thenReturn(null);

            mvc.perform(get("/api/seats/1")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testSaveSeat() throws Exception{
            Seat seat = new Seat();
            seat.setSeatIdentifier("A1");
            seat.setPriceMultiplier(1);
            seat.setRoom(null);

            Mockito.when(seatService.saveSeat(Mockito.any())).thenReturn(seat);

            mvc.perform(post("/api/seats")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(seat)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.seatIdentifier", is("A1")))
                    .andExpect(jsonPath("$.priceMultiplier", is(1)));
        }
}
