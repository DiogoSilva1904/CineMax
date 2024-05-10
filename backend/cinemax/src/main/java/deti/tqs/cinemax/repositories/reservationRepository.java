package deti.tqs.cinemax.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import deti.tqs.cinemax.models.reservation;

@Repository
public interface reservationRepository extends JpaRepository<reservation, Long>{

    
}
