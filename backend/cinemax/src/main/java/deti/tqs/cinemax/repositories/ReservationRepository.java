package deti.tqs.cinemax.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import deti.tqs.cinemax.models.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{

    List<Reservation> findByUserUsername(String username);

    
}
