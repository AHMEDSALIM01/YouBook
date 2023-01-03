package com.youbook.YouBook.services.serviceImplementation;

import com.youbook.YouBook.entities.Users;
import com.youbook.YouBook.repositories.UserRepository;
import com.youbook.YouBook.services.UserService;
import com.youbook.YouBook.validation.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {
    private UserRepository userRepository;
    private UserValidator userValidator;

    private PasswordEncoder passwordEncoder;

    public UserServiceImplementation(UserRepository userRepository, UserValidator userValidator,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);

    }

    @Override
    public Users updateUser(Long id, Users user) {
        Users userExist = this.getUserById(id);
        if(userExist==null){
            throw new IllegalStateException("utilisateur non trouvé");
        }

        if(!userValidator.validate(user)){
            throw new IllegalStateException(userValidator.getErrorMessage());
        }
        userExist.setName(user.getName());
        userExist.setAddress(user.getAddress());
        userExist.setPhoneNumber(user.getPhoneNumber());
        userExist.setPassword(passwordEncoder.encode(userExist.getPassword()));
        return userRepository.save(userExist);
    }

    @Override
    public Users bannUser(Long id) {
        Users userExist = this.getUserById(id);
        if(userExist==null){
            throw new IllegalStateException("utilisateur non trouvé");
        }
        userExist.setIs_active(false);
        return userRepository.save(userExist);
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

    @Override
    public void addRoleToUser(String email, String roleName) {

    }

    @Override
    public Users loadUserByEmail(String email) {
        System.out.println(email);
        Boolean isValidEmail = userValidator.validateEmail(email);
        if(!isValidEmail){
            throw new IllegalStateException(userValidator.getErrorMessage());
        }
        Users userByEmail = userRepository.findByEmail(email);
        if(userByEmail == null){
            throw new IllegalStateException("l'adresse email ou password est invalid");
        }
        return userByEmail;
    }
}
