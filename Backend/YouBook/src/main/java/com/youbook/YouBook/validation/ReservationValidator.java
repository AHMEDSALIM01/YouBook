package com.youbook.YouBook.validation;

import com.youbook.YouBook.entities.Hotel;
import com.youbook.YouBook.entities.Reservation;
import com.youbook.YouBook.entities.Room;
import com.youbook.YouBook.services.HotelService;
import com.youbook.YouBook.services.RoomService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
        if (reservation.getStartDate().isBefore(LocalDate.now())) {
            errorMessage="La date de début ne doit pas être avant la date d'aujourd'huit";
            return false;
        }
        if (reservation.getStartDate().isAfter(reservation.getEndDate())) {
            errorMessage="La date de début doit être avant la date de fin";
            return false;
        }
        if(reservation.getTotalPrice() == null){
            errorMessage="le prix total ne doit pas être vide";
        }
         if(reservation.getRoom() == null || reservation.getRoom().getId() == null) {
            errorMessage="La chambre est invalide";
            return false;
        }else{
            Room room = roomService.getRoomById(reservation.getRoom().getId());
            Hotel hotel = hotelService.getById(room.getHotel().getId());

            int numberOfDays = (int) ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
            System.out.println(numberOfDays);
            Double totalPrice =Double.valueOf(Math.round(room.getPrice()*numberOfDays*100)/100d);
            System.out.println(totalPrice);
            System.out.println(reservation.getTotalPrice());
            if(!totalPrice.equals(reservation.getTotalPrice())){
                errorMessage ="le Prix total invalide";
                return false;
            }
            if(room == null){
                errorMessage="Chambre non trouvée";
                return false;
            }

            if(hotel == null){
                errorMessage="Hotel non trouvé";
                return false;
            }

            if (!hotel.getId().equals(room.getHotel().getId())) {
                errorMessage="La chambre n'appartient pas à l'hôtel de la réservation";
                return false;
            }
        }
        if (reservation.getUser() == null || reservation.getUser().getId() == null) {
            errorMessage="L'utilisateur est invalide";
            return false;
        }

        errorMessage="";
        return true;
    }


    public String getErrorMessage() {
        return errorMessage;
    }
}
