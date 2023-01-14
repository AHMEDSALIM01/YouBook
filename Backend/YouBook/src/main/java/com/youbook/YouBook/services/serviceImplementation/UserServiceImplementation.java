package com.youbook.YouBook.services.serviceImplementation;

import com.youbook.YouBook.dtos.UserDto;
import com.youbook.YouBook.entities.Role;
import com.youbook.YouBook.entities.Users;
import com.youbook.YouBook.repositories.UserRepository;
import com.youbook.YouBook.services.RoleService;
import com.youbook.YouBook.services.UserService;
import com.youbook.YouBook.validation.UserValidator;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {
    private UserRepository userRepository;
    private UserValidator userValidator;

    private PasswordEncoder passwordEncoder;
    private RoleService roleService;


    public UserServiceImplementation(UserRepository userRepository, RoleService roleService, UserValidator userValidator, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;

    }

    @Override
    public Users signUp(String roleName, Users user) {
        Boolean isValidUser = userValidator.validate(user);
        if (!isValidUser) {
            throw new IllegalStateException(userValidator.getErrorMessage());
        }
        Users userByEmail = userRepository.findByEmail(user.getEmail());
        if (userByEmail != null) {
            throw new IllegalStateException("utilisateur existe déja");
        }
        if (roleName == null) {
            throw new IllegalStateException("nom de role ne doit pas être vide ");
        }
        if (roleName == "ADMIN") {
            throw new IllegalStateException("nom de role Invalid");
        }
        Role role = roleService.getRoleByName(roleName);
        if (role == null) {
            throw new IllegalStateException("role non trouvée");
        }
        user.getRoles().add(role);
        user.setIs_active(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Users addUser(UserDto userDto) {
        Users user = new Users();
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setAddress(userDto.getAddress());
        user.setEmail(userDto.getEmail());
        Boolean isValidUser = userValidator.validate(user);
        if (!isValidUser) {
            throw new IllegalStateException(userValidator.getErrorMessage());
        }
        Users userByEmail = userRepository.findByEmail(user.getEmail());
        if (userByEmail != null) {
            throw new IllegalStateException("utilisateur existe déja");
        }
        Role roleCheck = roleService.getRoleByName(userDto.getRoleName());
        if (roleCheck == null) {
            throw new IllegalStateException("role non trouvée");
        }

        user.setIs_active(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Users userSaved = userRepository.save(user);
        Users userWithRole = this.addRoleToUser(user.getEmail(), roleCheck.getName());
        return userWithRole;

    }

    @Override
    public Users updateUser(Long id, Users user) {
        Users userExist = this.getUserById(id);
        if (userExist == null) {
            throw new IllegalStateException("utilisateur non trouvé");
        }

        if (!userValidator.validate(user)) {
            throw new IllegalStateException(userValidator.getErrorMessage());
        }
        userExist.setName(user.getName());
        if (user.getIs_active() != null) {
            userExist.setIs_active(user.getIs_active());
        }
        userExist.setAddress(user.getAddress());
        userExist.setPhoneNumber(user.getPhoneNumber());
        userExist.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(userExist);
    }

    @Override
    public Users bannUser(Long id) {
        Users userExist = this.getUserById(id);
        if (userExist == null) {
            throw new IllegalStateException("utilisateur non trouvé");
        }
        userExist.setIs_active(false);
        return userRepository.save(userExist);
    }

    @Override
    public Users getUserById(Long id) {
        Optional<Users> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new IllegalStateException("utilisateur non trouvée");
        }
    }

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Users addRoleToUser(String email, String roleName) {
        Users user = this.loadUserByEmail(email);
        Role role = roleService.getRoleByName(roleName);
        if (user.getRoles() == null) {
            throw new IllegalStateException("role is null");
        }
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    @Override
    public Users loadUserByEmail(String email) {
        Boolean isValidEmail = userValidator.validateEmail(email);
        if (!isValidEmail) {
            throw new IllegalStateException(userValidator.getErrorMessage());
        }
        Users userByEmail = userRepository.findByEmail(email);
        if (userByEmail == null) {
            throw new IllegalStateException("l'adresse email invalid");
        }
        return userByEmail;
    }

}