package com.youbook.YouBook.controllers;

import com.youbook.YouBook.entities.Users;
import com.youbook.YouBook.repositories.UserRepository;
import com.youbook.YouBook.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/addUser")
    public ResponseEntity saveUser(@Validated @RequestBody Users user){
        Users user1 = userService.addUser(user);
        if(user1 != null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(user1);
        }else {
            return ResponseEntity.badRequest().body("Donnés non valid");
        }
    }
}
