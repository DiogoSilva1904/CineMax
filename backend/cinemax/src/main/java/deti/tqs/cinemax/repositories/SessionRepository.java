package deti.tqs.cinemax.repositories;

import org.springframework.stereotype.Repository;

import deti.tqs.cinemax.models.Session;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long>{

    List<Session> findByDate(String date);
    
}
