package com.youbook.YouBook.validation;

import com.youbook.YouBook.entities.Hotel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HotelValidatore {
    private String errorMessage;

    public boolean validate(Hotel hotel) {
        Pattern patternAddress= Pattern.compile("^([0-9]*) ?([a-zA-Z,\\. ]*) ?([0-9]{5}) ?([a-zA-Z]*)$");
        Pattern patternCity = Pattern.compile("^[A-Za-z]{3,15}");
        Matcher matcherAddress = patternAddress.matcher(hotel.getAddress());
        Matcher matcherCity = patternCity.matcher(hotel.getCity());
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
        if(!matcherAddress.matches()){
            errorMessage = "format de l'adresse est invalide";
            return false;
        }
        if(!matcherCity.matches()){
            errorMessage = "format de ville invalide";
            return false;
        }
        errorMessage = "";
        return true;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
