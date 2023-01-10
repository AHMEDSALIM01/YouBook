package com.youbook.YouBook.services.serviceImplementation;

import com.youbook.YouBook.entities.Reservation;
import com.youbook.YouBook.entities.Room;
import com.youbook.YouBook.entities.Users;
import com.youbook.YouBook.enums.StatusReservation;
import com.youbook.YouBook.repositories.ReservationRepository;
import com.youbook.YouBook.services.HotelService;
import com.youbook.YouBook.services.ReservationService;
import com.youbook.YouBook.services.RoomService;
import com.youbook.YouBook.services.UserService;
import com.youbook.YouBook.validation.ReservationValidator;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImplementation implements ReservationService {
    private UserService userService;
    private HotelService hotelService;
    private RoomService roomService;
    private ReservationRepository reservationRepository;
    private ReservationValidator reservationValidator;

    public ReservationServiceImplementation(UserService userService,HotelService hotelService, RoomService roomService, ReservationRepository reservationRepository,ReservationValidator reservationValidator) {
        this.userService=userService;
        this.hotelService = hotelService;
        this.roomService = roomService;
        this.reservationRepository = reservationRepository;
        this.reservationValidator=reservationValidator;
    }

    @Override
    public Reservation addReservation(Reservation reservation) {

        if (!reservationValidator.validate(reservation)) {
            throw new IllegalStateException(reservationValidator.getErrorMessage());
        }

        Users user = userService.getUserById(reservation.getUser().getId());
        if (user == null) {
            throw new IllegalStateException("utilisateur est invalide");
        }

        Reservation reservationToCheck = reservationRepository.getReservationByRef(reservation.getRef());
        if (reservationToCheck != null) {
            throw new IllegalStateException("Réservation existe déjà");
        }
        Boolean isHotelAvailable = hotelService.isHotelAvailable(reservation);
        if(!isHotelAvailable){
            throw new IllegalStateException("L'hotel n'est pas disponible pour la période donnée");
        }

        Boolean isRoomAvailable = roomService.isRoomAvailable(reservation);
        if (!isRoomAvailable) {
            throw new IllegalStateException("La chambre n'est pas disponible pour la période donnée");
        }
        int numberOfDays = (int) ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
        Double totalPrice =Double.valueOf(Math.round(reservation.getRoom().getPrice()*numberOfDays*100)/100d);
        reservation.setStatus(StatusReservation.En_cours);
        reservation.setTotalPrice(totalPrice);
        Reservation savedReservation = reservationRepository.save(reservation);
        Room room = roomService.getRoomById(savedReservation.getRoom().getId());
        room.getReservations().add(savedReservation);
        user.getReservations().add(savedReservation);
        userService.updateUser(user.getId(),user);
        roomService.updateRoom(room);
        return savedReservation;
    }

    @Override
    public Reservation updateReservation(String ref, Reservation reservation) {
        if (!reservationValidator.validate(reservation)) {
            throw new IllegalStateException(reservationValidator.getErrorMessage());
        }
        Users user = userService.getUserById(reservation.getUser().getId());
        if (user == null) {
            throw new IllegalStateException("utilisateur est invalide");
        }
        Reservation reservationToCheck = reservationRepository.getReservationByRef(ref);
        if (reservationToCheck == null) {
            throw new IllegalStateException("Réservation non trouvée");
        }
        if(reservationToCheck.getStatus() == StatusReservation.valueOf("Confirmée")){
            throw new IllegalStateException("vous n'avez pas le droit de modifier cette resérvation car elle est déjâ confirmée");
        }
        Boolean isHotelAvailable = hotelService.isHotelAvailable(reservation);
        if(!isHotelAvailable){
            throw new IllegalStateException("L'hotel n'est pas disponible pour la période donnée");
        }
        Boolean isRoomAvailable = roomService.isRoomAvailable(reservation);
        if (!isRoomAvailable) {
            throw new IllegalStateException("La chambre n'est pas disponible pour la période donnée");
        }
        int numberOfDays = (int) ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
        Double totalPrice =Double.valueOf(Math.round(reservation.getRoom().getPrice()*numberOfDays*100)/100d);
        reservationToCheck.setTotalPrice(totalPrice);
        reservationToCheck.setStartDate(reservation.getStartDate());
        reservationToCheck.setEndDate(reservation.getEndDate());
        reservationToCheck.setRoom(reservation.getRoom());
        Reservation savedReservation = reservationRepository.save(reservationToCheck);
        return savedReservation;
    }

    @Override
    public Reservation confirmReservation(Reservation reservation) {
        Reservation reservationToCheck = reservationRepository.getReservationByRef(reservation.getRef());
        if (reservationToCheck == null) {
            throw new IllegalStateException("Réservation non trouvée");
        }
        if(reservationToCheck.getStatus()==StatusReservation.valueOf("Annulée")){
            throw new IllegalStateException("Réservation annulé");
        }
        reservationToCheck.setStatus(StatusReservation.valueOf("Confirmé"));
        return reservationRepository.save(reservationToCheck);
    }

    @Override
    public Reservation cancelReservation(Reservation reservation) {
        Reservation reservationToCheck = reservationRepository.getReservationByRef(reservation.getRef());

        if (reservationToCheck == null) {
            throw new IllegalStateException("Réservation non trouvée");
        }
        Room room = reservationToCheck.getRoom();
        List<Reservation> reservations = room.getReservations();
        reservations.remove(reservationToCheck);
        reservationToCheck.setStatus(StatusReservation.valueOf("Annulée"));
        room.getReservations().add(reservationToCheck);
        roomService.updateRoom(room);
        return reservationRepository.save(reservationToCheck);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation getReservationByRef(String ref) {
        Reservation reservation = reservationRepository.getReservationByRef(ref);
        if(reservation == null){
            throw new IllegalStateException("resérvation non trouvée");
        }
        return reservation;
    }

    @Override
    public Reservation getReservationById(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (!reservation.isPresent()){
            throw new IllegalStateException("réservation non trouvés");
        }
        return reservation.get();
    }
}
