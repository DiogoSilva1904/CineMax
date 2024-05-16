package deti.tqs.cinemax.controllers;

import deti.tqs.cinemax.models.Room;
import deti.tqs.cinemax.services.RoomService;
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

@WebMvcTest(RoomController.class)
class RoomControllerTest {

        @Autowired
        private MockMvc mvc;

        @MockBean
        private RoomService roomService;

        private ObjectMapper objectMapper = new ObjectMapper();


        @Test
        void testGetAllRooms() throws Exception{
            Room room = new Room();
            room.setName("Room 1");
            room.setCapacity(100);
            room.setType("Normal");
            room.setSessions(null);

            Room room1 = new Room();
            room1.setName("Room 2");
            room1.setCapacity(80);
            room1.setType("IMAX");
            room1.setSessions(null);

            Room room2 = new Room();
            room2.setName("Room 3");
            room2.setCapacity(80);
            room2.setType("IMAX");
            room2.setSessions(null);

            Mockito.when(roomService.getAllRooms()).thenReturn(List.of(room, room1, room2));

            mvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("Room 1")))
                .andExpect(jsonPath("$[0].capacity", is(100)))
                .andExpect(jsonPath("$[1].name", is("Room 2")))
                .andExpect(jsonPath("$[1].capacity", is(80)))
                .andExpect(jsonPath("$[2].name", is("Room 3")))
                .andExpect(jsonPath("$[2].capacity", is(80)));


        }



        @Test
        void testGetRoomById() throws Exception {
            Room room = new Room();
            room.setName("Room 1");
            room.setCapacity(100);
            room.setType("Normal");
            room.setSessions(null);

            Mockito.when(roomService.getRoomById(1L)).thenReturn(room);

            mvc.perform(get("/api/rooms/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Room 1")))
                .andExpect(jsonPath("$.capacity", is(100)));
        }

        @Test
        void testGetRoomByIdNotFound() throws Exception {
            Mockito.when(roomService.getRoomById(1L)).thenReturn(null);

            mvc.perform(get("/api/rooms/1"))
                .andExpect(status().isNotFound());
        }

        @Test
        void testSaveRoom() throws Exception {
            Room room = new Room();
            room.setName("Room 1");
            room.setCapacity(100);
            room.setType("Normal");
            room.setSessions(null);

            Mockito.when(roomService.saveRoom(Mockito.any())).thenReturn(room);

            mvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(room)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Room 1")))
                .andExpect(jsonPath("$.capacity", is(100)));
        }


}
