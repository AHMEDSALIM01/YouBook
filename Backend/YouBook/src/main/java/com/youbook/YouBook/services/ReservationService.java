package com.youbook.YouBook.services;

import com.youbook.YouBook.entities.Reservation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReservationService {
    Reservation addReservation(Long user_id,Reservation reservation);
    Reservation updateReservation(Long user_id,String ref,Reservation reservation);
    Reservation confirmReservation(String ref);
    Reservation cancelReservation(String ref);
    List<Reservation> getAllReservations();
    Reservation getReservationByRef(String ref);
    Reservation getReservationById(Long id);
}
