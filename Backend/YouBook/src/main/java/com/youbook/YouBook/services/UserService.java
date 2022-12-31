package com.youbook.YouBook.services;

import com.youbook.YouBook.entities.Users;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    Users addUser(Users user);
    Users updateUser(Long id,Users user);
    Users bannUser(Long id);
    Users getUserById(Long id);
    List<Users> getAllUsers();

}
