package com.youbook.YouBook.services;

import com.youbook.YouBook.dtos.UserDto;
import com.youbook.YouBook.entities.Users;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public interface UserService {
    Users signUp(String roleName,Users user);
    Users addUser(UserDto user);
    Users updateUser(Long id,Users user);
    Users bannUser(Long id);
    Users getUserById(Long id);
    List<Users> getAllUsers();
    Users addRoleToUser(String email,String roleName);
    Users loadUserByEmail(String email);
}
