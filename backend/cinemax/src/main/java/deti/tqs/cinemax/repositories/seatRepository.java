package deti.tqs.cinemax.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deti.tqs.cinemax.models.seat;

@Repository
public interface seatRepository extends JpaRepository<seat, Long>{
    
}
