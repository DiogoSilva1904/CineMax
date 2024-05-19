package deti.tqs.cinemax.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;

    private String time;

    @ManyToOne
    @JoinColumn(name= "movie_id")
    @JsonIgnoreProperties("session")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonIgnoreProperties("sessions")
    private Room room;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("session")
    private List<Reservation> reservation;

    @ElementCollection  // this is and array with the seats occupied example ["A2","A1"] etc...
    private List<String> bookedSeats;
    
}
