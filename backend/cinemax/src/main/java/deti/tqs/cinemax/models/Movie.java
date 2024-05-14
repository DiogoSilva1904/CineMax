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
@Table(name = "Movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    private String category;

    private String genre;

    private String studio;

    private String duration;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Session> session;
    
}