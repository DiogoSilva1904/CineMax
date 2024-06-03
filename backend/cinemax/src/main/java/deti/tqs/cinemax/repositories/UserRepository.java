package deti.tqs.cinemax.repositories;

import org.springframework.stereotype.Repository;

import deti.tqs.cinemax.models.AppUser;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long>{

    AppUser findByUsername(String username);

    
}
