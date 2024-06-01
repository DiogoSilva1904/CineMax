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

    private int rows; //pd ser util no frontend

    private int columns; //same

    private String type;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<session> sessions;
    
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<seat> seats;
}