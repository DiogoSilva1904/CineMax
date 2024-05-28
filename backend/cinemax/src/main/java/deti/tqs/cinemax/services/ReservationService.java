package deti.tqs.cinemax.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import deti.tqs.cinemax.models.Session;

import deti.tqs.cinemax.models.Reservation;
import deti.tqs.cinemax.repositories.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.web.servlet.View;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final SessionService sessionService;

    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);
    private final View error;

    public ReservationService(ReservationRepository reservationRepository, SessionService sessionService, View error) {
        this.reservationRepository = reservationRepository;
        this.sessionService = sessionService;
        this.error = error;
    }

    public void deleteReservation(Long id) {
        log.info("Deleting reservation with id {}", id);
        Session session = sessionService.getSessionById(reservationRepository.findById(id).get().getSession().getId());
        List<String> bookedSeats = session.getBookedSeats();
        List<String> selectedSeats = reservationRepository.findById(id).get().getSeatNumbers();
        bookedSeats.removeAll(selectedSeats);
        session.setBookedSeats(bookedSeats);
        //session.setAvailableSeats(session.getAvailableSeats() + selectedSeats.size());
        reservationRepository.deleteById(id);
    }

    @Transactional
    public Reservation saveReservation(Reservation reservation) {
        List<String> selectedSeats = reservation.getSeatNumbers();
        Session session = sessionService.getSessionById(reservation.getSession().getId());

        if (session != null) {
            List<String> bookedSeats = session.getBookedSeats();
            for (String seat : selectedSeats) {
                if (bookedSeats.contains(seat)) {
                    log.info("Seat {} is already booked", seat);
                    //throw new SeatAlreadyBookedException("Seat " + seat + " is already booked");
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

    public Reservation getReservationById(Long id) {
        log.info("Retrieving reservation with id {}", id);
        return reservationRepository.findById(id).orElse(null);
    }

    public Optional<Reservation> updateReservation(Long id, Reservation reservation) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(id);

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
