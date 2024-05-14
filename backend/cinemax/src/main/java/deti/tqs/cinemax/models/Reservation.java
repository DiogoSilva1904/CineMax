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
@Table(name = "Reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@ManyToOne
    //@JoinColumn(name = "user_id")
    //private User user;

    private String username; //cant use user

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    @ElementCollection
    private List<String> seatNumbers;
}
