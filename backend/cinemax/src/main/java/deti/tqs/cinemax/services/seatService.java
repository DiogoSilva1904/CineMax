package deti.tqs.cinemax.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import deti.tqs.cinemax.models.seat;
import deti.tqs.cinemax.repositories.seatRepository;

@Service
public class seatService {

    private final seatRepository seatRepository;

    private static final Logger log = LoggerFactory.getLogger(seatService.class);

    public seatService(seatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public void deleteSeat(Long id) {
        log.info("Deleting seat with id {}", id);
        seatRepository.deleteById(id);
    }

    public Optional<seat> updateSeat(Long id, seat seat) {
        Optional<seat> seatOptional = seatRepository.findById(id);
        if (seatOptional.isPresent()) {
            log.info("Updating seat with id {}", id);
            seat.setId(id);
            return Optional.of(seatRepository.save(seat));
        }
        else {
            log.info("Seat with id {} not found", id);
        }

        return seatOptional;
    }

    public seat saveSeat(seat seat) {
        log.info("Saving seat with id {}", seat.getId());
        return seatRepository.save(seat);
    }

    public seat getSeatById(Long id) {
        log.info("Retrieving seat with id {}", id);
        return seatRepository.findById(id).orElse(null);
    }

    public List<seat> getAllSeats() {
        log.info("Retrieving all seats");
        return seatRepository.findAll();
    }
}
