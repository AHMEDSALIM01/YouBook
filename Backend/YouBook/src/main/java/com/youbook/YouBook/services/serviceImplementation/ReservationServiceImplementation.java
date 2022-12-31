package com.youbook.YouBook.services.serviceImplementation;

import com.youbook.YouBook.entities.Reservation;
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
    public Reservation addReservation(Long user_id, Reservation reservation) {
        Boolean isValidReservation = reservationValidator.validate(reservation);
        Users users = userService.getUserById(user_id);
        if(isValidReservation){
            if(users !=null){
                reservation.setUser(users);
                reservation.setStatus(StatusReservation.En_cours);
                return reservationRepository.save(reservation);
            }else{
                throw new IllegalStateException("utilisateur est invalide");
            }
        }else {
            throw new IllegalStateException(reservationValidator.getErrorMessage());
        }
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
