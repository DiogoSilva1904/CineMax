package deti.tqs.cinemax.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deti.tqs.cinemax.models.movie;

@Repository
public interface movieRepository extends JpaRepository<movie, Long>{

}
