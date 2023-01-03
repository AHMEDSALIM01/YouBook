package com.youbook.YouBook.validation;

import com.youbook.YouBook.entities.Users;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {
    private String errorMessage;

    public Boolean validate(Users user) {
        if (user.getPassword() == null || !user.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")) {
            errorMessage = "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule et un chiffre";
            return false;
        }
        if (user.getEmail() == null || !user.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            errorMessage ="L'adresse email est invalide";
            return false;
        }
        if (user.getPhoneNumber() == null || !user.getPhoneNumber().matches("^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$")) {
            errorMessage = "Le numéro de téléphone est invalide";
            return false;
        }
        if (user.getAddress() == null || user.getAddress().length() < 5) {
            errorMessage = "L'adresse doit contenir au moins 5 caractères";
            return false;
        }
        return true;
    }
    public Boolean validateEmail(String email){
        System.out.println(email);
        if(email == null || !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")){
            errorMessage ="L'adresse email est invalide";
            return false;
        }
        errorMessage = "";
        return true;
    }

    public Boolean validatePassword(String password){
        if (password == null || password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")) {
            errorMessage = "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule et un chiffre";
            return false;
        }
        errorMessage = "";
        return true;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
