package deti.tqs.cinemax.models;

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
@Table(name = "Seats")
public class Seat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatIdentifier;

    //book status shouldnt be in here bcs it makes impossible the reuse of a seat for multiple sessions

    private int priceMultiplier; //maybe remove idk (vip ticket mais caro)

    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonIgnoreProperties("seats")
    private Room room;

}
