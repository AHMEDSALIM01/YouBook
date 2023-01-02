package com.youbook.YouBook.validation;

import com.youbook.YouBook.entities.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomValidator {
    private String errorMessage;
    public Boolean validate(Room room){
        if(room.getNumber()<1){
            errorMessage="le numéro de chambre doit etre supérieur ou égale 1";
            return false;
        }
        if(room.getNumberOfBeds()<1){
            errorMessage="le nombre de lit dans la chambre doit etre supérieur ou égale 1";
            return false;
        }
        if (room.getPrice()==null || room.getPrice().isNaN()){
            errorMessage="le prix est obligatoire est doit etre un nombre réel";
            return false;
        }
        if(room.getPrice()<=0){
            errorMessage="le prix doit etre suprieur strictement à 0";
            return false;
        }
        errorMessage="";
        return true;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
