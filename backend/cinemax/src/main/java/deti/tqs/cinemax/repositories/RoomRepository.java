package deti.tqs.cinemax.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deti.tqs.cinemax.models.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>{
    
}
