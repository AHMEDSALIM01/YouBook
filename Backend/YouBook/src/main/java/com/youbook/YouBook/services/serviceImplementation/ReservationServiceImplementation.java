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
import org.springframework.stereotype.Component;

import java.util.List;
@Component
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

        reservation.setStatus(StatusReservation.En_cours);
        Reservation savedReservation = reservationRepository.save(reservation);
        Room room = roomService.getRoomById(savedReservation.getRoom().getId());
        room.getReservations().add(savedReservation);
        roomService.updateRoom(room);
        return savedReservation;
    }

    @Override
    public Reservation updateReservation(Long user_id, String ref, Reservation reservation) {
        return null;
    }

    @Override
    public Reservation confirmReservation(String ref) {
        return null;
    }

    @Override
    public Reservation cancelReservation(String ref) {
        return null;
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation getReservationByRef(String ref) {
        return null;
    }

    @Override
    public Reservation getReservationById(Long id) {
        return null;
    }
}
