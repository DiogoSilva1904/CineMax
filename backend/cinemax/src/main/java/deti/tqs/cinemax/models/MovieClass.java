package deti.tqs.cinemax.models;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor  
public class MovieClass {

    private String title;

    private String duration;

    private String studio;

    private String genre;

    private MultipartFile image;
    
}
