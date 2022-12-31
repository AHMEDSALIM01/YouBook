package com.youbook.YouBook.validation;

import com.youbook.YouBook.entities.Hotel;
import com.youbook.YouBook.entities.Reservation;
import com.youbook.YouBook.entities.Room;
import com.youbook.YouBook.services.HotelService;
import com.youbook.YouBook.services.RoomService;
import org.springframework.stereotype.Component;

@Component
public class ReservationValidator {
    private HotelService hotelService;
    private RoomService roomService;
    private String errorMessage;

    public ReservationValidator(HotelService hotelService, RoomService roomService) {
        this.hotelService = hotelService;
        this.roomService = roomService;
    }

    public Boolean validate(Reservation reservation) {
        if (reservation.getStartDate().isAfter(reservation.getEndDate())) {
            errorMessage="La date de début doit être avant la date de fin";
            return false;
        }
        if (reservation.getRoom() == null || reservation.getRoom().getId() == null) {
            errorMessage="La chambre est invalide";
            return false;
        }
        if (reservation.getUser() == null || reservation.getUser().getId() == null) {
            errorMessage="L'utilisateur est invalide";
            return false;
        }

        Hotel hotel = hotelService.getById(reservation.getRoom().getHotel().getId());
        if(hotel == null){
            errorMessage="Hotel non trouvé";
            return false;
        }

        Room room = roomService.getRoomById(reservation.getRoom().getId());
        if(room == null){
            errorMessage="Chambre non trouvée";
            return false;
        }
        if (!hotel.getId().equals(room.getHotel().getId())) {
            errorMessage="La chambre n'appartient pas à l'hôtel de la réservation";
            return false;
        }
        return true;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
