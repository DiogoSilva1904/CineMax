package deti.tqs.cinemax.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import deti.tqs.cinemax.models.session;

import deti.tqs.cinemax.models.reservation;
import deti.tqs.cinemax.repositories.reservationRepository;
import jakarta.transaction.Transactional;

@Service
public class reservationService {

    private final reservationRepository reservationRepository;

    private final sessionService sessionService;

    private static final Logger log = LoggerFactory.getLogger(reservationService.class);

    public reservationService(reservationRepository reservationRepository, sessionService sessionService) {
        this.reservationRepository = reservationRepository;
        this.sessionService = sessionService;
    }

    public void deleteReservation(Long id) {
        log.info("Deleting reservation with id {}", id);
        reservationRepository.deleteById(id);
    }

    @Transactional
    public reservation saveReservation(reservation reservation) {
        List<String> selectedSeats = reservation.getSeatNumbers();
        session session = sessionService.getSessionById(reservation.getSession().getId());

        if (session != null) {
            List<String> bookedSeats = session.getBookedSeats();
            for (String seat : selectedSeats) {
                if (bookedSeats.contains(seat)) {
                    log.info("Seat {} is already booked", seat);
                    return null;
                }
            }
            bookedSeats.addAll(selectedSeats);
            session.setBookedSeats(bookedSeats);
            sessionService.updateSession(session.getId(), session);
        }

        log.info("Saving reservation with id {}", reservation.getId());
        return reservationRepository.save(reservation);

    }

    public reservation getReservationById(Long id) {
        log.info("Retrieving reservation with id {}", id);
        return reservationRepository.findById(id).orElse(null);
    }

    public Optional<reservation> updateReservation(Long id, reservation reservation) {
        Optional<reservation> reservationOptional = reservationRepository.findById(id);

        if (reservationOptional.isPresent()) {
            log.info("Updating reservation with id {}", id);
            reservation.setId(id);
            return Optional.of(reservationRepository.save(reservation));
        } else {
            log.info("Reservation with id {} not found", id);
        }
        return reservationOptional;
    }

    
}
