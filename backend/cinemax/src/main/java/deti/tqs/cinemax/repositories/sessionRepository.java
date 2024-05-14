package deti.tqs.cinemax.repositories;

import org.springframework.stereotype.Repository;

import deti.tqs.cinemax.models.Session;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface sessionRepository extends JpaRepository<Session, Long>{
    
}
