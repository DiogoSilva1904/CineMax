package deti.tqs.cinemax.models;

import java.util.List;

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
public class session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;

    private String time;

    @ManyToOne
    @JoinColumn(name= "movie_id")
    private movie movie;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private room room;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<reservation> reservation;

    @ElementCollection  // this is and array with the seats occupied example ["A2","A1"] etc...
    private List<String> bookedSeats;
    
}
