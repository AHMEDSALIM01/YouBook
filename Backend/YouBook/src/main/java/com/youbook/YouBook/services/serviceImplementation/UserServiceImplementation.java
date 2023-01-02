package com.youbook.YouBook.services.serviceImplementation;

import com.youbook.YouBook.entities.Users;
import com.youbook.YouBook.repositories.UserRepository;
import com.youbook.YouBook.services.UserService;
import com.youbook.YouBook.validation.UserValidator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserServiceImplementation implements UserService {
    private UserRepository userRepository;
    private UserValidator userValidator;

    public UserServiceImplementation(UserRepository userRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    @Override
    public Users addUser(Users user) {
        Boolean isValidUser = userValidator.validate(user);
        if(!isValidUser){
            throw new IllegalStateException(userValidator.getErrorMessage());
        }
        Users userByEmail = userRepository.findByEmail(user.getEmail());
        if(userByEmail !=null){
            throw new IllegalStateException("utilisateur existe déja");
        }
        user.setIs_active(true);
        return userRepository.save(user);

    }

    @Override
    public Users updateUser(Long id, Users user) {
        Users userExist = this.getUserById(id);
        if(userExist!=null){
            userExist = user;
            if(userValidator.validate(userExist)){
                return userRepository.save(userExist);
            }else{
                throw new IllegalStateException(userValidator.getErrorMessage());
            }
        }else {
            throw new IllegalStateException("utilisateur non trouvé");
        }
    }

    @Override
    public Users bannUser(Long id) {
        Users userExist = this.getUserById(id);
        if(userExist!=null){
            userExist.setIs_active(false);
            return userRepository.save(userExist);
        }else {
            throw new IllegalStateException("utilisateur non trouvé");
        }
    }

    @Override
    public Users getUserById(Long id) {
        Optional<Users> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }else{
            throw new IllegalStateException("utilisateur non trouvée");
        }
    }

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }
}
