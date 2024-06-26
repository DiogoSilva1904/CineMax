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
@Table(name = "Reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("reservations")
    private AppUser user;

    private int price;

    private boolean used;

    @ManyToOne
    @JoinColumn(name = "session_id")
    @JsonIgnoreProperties("reservation")
    private Session session;

    @ElementCollection
    private List<String> seatNumbers;
    
}
