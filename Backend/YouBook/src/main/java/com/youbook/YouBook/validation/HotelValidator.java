package com.youbook.YouBook.validation;

import com.youbook.YouBook.entities.Hotel;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class HotelValidator {

    private String errorMessage;

    public boolean validate(Hotel hotel) {
        if (hotel.getName() == null || hotel.getName().isEmpty()) {
            errorMessage = "Le nom de l'hôtel est obligatoire";
            return false;
        }
        if (hotel.getAddress() == null || hotel.getAddress().isEmpty()) {
            errorMessage = "L'adresse de l'hôtel est obligatoire";
            return false;
        }
        if (hotel.getCity() == null || hotel.getCity().isEmpty()) {
            errorMessage = "La ville de l'hôtel est obligatoire";
            return false;
        }
        if (hotel.getNumberOfRooms() < 1) {
            errorMessage = "Le nombre de chambres de l'hôtel doit être supérieur à 0";
            return false;
        }

        errorMessage = "";
        return true;
    }
    public Boolean validDate(LocalDate startDate,LocalDate endDate){
        if (startDate.isBefore(LocalDate.now())){
            errorMessage ="la date de début ne doit pas être avant la date d'aujourd'huit'";
            return false;
        }
        if (startDate.isAfter(endDate)){
            errorMessage ="la date de début ne doit pas être avant la date de fin";
            return false;
        }
        errorMessage="";
        return true;
    }

    public Boolean validateRoom(Hotel hotel){
        if (hotel.getRooms().isEmpty()){
            errorMessage="l'hotel ne contient auccune chambres";
            return false;
        }

        errorMessage="";
        return true;
    }

    public Boolean validateNumberOfRoom(Hotel hotel){
        if(hotel.getRooms().size() == hotel.getNumberOfRooms()){
            errorMessage="le nombre des chambre est saturés merci de modifier le nombre des chambre avant d'ajouter une autre chambre";
            return false;
        }
        errorMessage="";
        return true;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
}
