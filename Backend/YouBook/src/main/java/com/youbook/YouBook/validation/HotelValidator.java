package com.youbook.YouBook.validation;

import com.youbook.YouBook.entities.Hotel;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public String getErrorMessage() {
        return errorMessage;
    }
}
