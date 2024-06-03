package deti.tqs.cinemax.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import deti.tqs.cinemax.models.Session;
import deti.tqs.cinemax.models.AppUser;
import deti.tqs.cinemax.models.Reservation;
import deti.tqs.cinemax.repositories.ReservationRepository;
import jakarta.transaction.Transactional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final SessionService sessionService;

    private final UserService UserService;

    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);

    public ReservationService(ReservationRepository reservationRepository, SessionService sessionService, UserService UserService) {
        this.reservationRepository = reservationRepository;
        this.sessionService = sessionService;
        this.UserService = UserService;
    }

    /**
     * Deletes a reservation by its ID.
     *
     * @param id the ID of the reservation to delete.
     */
    public void deleteReservation(Long id) {
        log.info("Deleting reservation with id {}", id);
        Session session = sessionService.getSessionById(reservationRepository.findById(id).get().getSession().getId());
        List<String> bookedSeats = session.getBookedSeats();
        List<String> selectedSeats = reservationRepository.findById(id).get().getSeatNumbers();
        bookedSeats.removeAll(selectedSeats);
        session.setBookedSeats(bookedSeats);
        reservationRepository.deleteById(id);
    }

    /**
     * Saves a reservation to the repository.
     *
     * @param reservation the reservation to save.
     * @return the saved reservation, or null if any of the selected seats are already booked.
     */
    @Transactional
    public Reservation saveReservation(Reservation reservation) {
        List<String> selectedSeats = reservation.getSeatNumbers();
        Session session = sessionService.getSessionById(reservation.getSession().getId());
        AppUser user = UserService.getUserByUsername(reservation.getUser().getUsername());
        reservation.setUser(user);
        reservation.setUsed(false);

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

    /**
     * Retrieves a reservation by its ID.
     *
     * @param id the ID of the reservation.
     * @return the reservation with the specified ID, or null if not found.
     */
    public Reservation getReservationById(Long id) {
        log.info("Retrieving reservation with id {}", id);
        return reservationRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves all reservations made by a specific user.
     *
     * @param username the username of the user.
     * @return a list of reservations made by the user.
     */
    public List<Reservation> getReservationsByUser(String username) {
        log.info("Retrieving reservations by user {}", username);
        return reservationRepository.findByUserUsername(username);
    }

    /**
     * Updates an existing reservation.
     *
     * @param id          the ID of the reservation to update.
     * @param reservation the updated reservation details.
     * @return an Optional containing the updated reservation if successful, or an empty Optional if the reservation was not found.
     */
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

    /**
     * Retrieves all reservations from the repository.
     *
     * @return a list of all reservations.
     */
    public List<Reservation> getAllReservations() {
        log.info("Retrieving all reservations");
        return reservationRepository.findAll();
    }

    /**
     * Marks a reservation as used by its ID.
     *
     * @param id the ID of the reservation to mark as used.
     * @return the updated reservation, or null if the reservation was not found or was already used.
     */
    public Reservation makeReservationUsed(Long id) {
        log.info("Making reservation with id {} used", id);
        Reservation reservation = reservationRepository.findById(id).orElse(null);
        if (reservation != null && !reservation.isUsed()) {
            reservation.setUsed(true);
            return reservationRepository.save(reservation);
        }
        return null;
    }

}
