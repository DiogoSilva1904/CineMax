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
@Table(name = "Rooms")
public class room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private int capacity;

    @Column(nullable = false,name = "`rows`") // Wrapping "rows" in backticks to avoid SQL syntax error
    private int numberOfRows; // Renamed to avoid conflict with reserved keyword

    private int columns; //same

    private String type;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<session> sessions;
    
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<seat> seats;
}
