package deti.tqs.cinemax.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deti.tqs.cinemax.models.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long>{
    
}
