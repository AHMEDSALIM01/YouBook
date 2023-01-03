package com.youbook.YouBook.controllers;

import com.youbook.YouBook.entities.Users;
import com.youbook.YouBook.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    private UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/signUp")
    public ResponseEntity signUp(@Validated @RequestBody Users user){
        Users user1 = userService.addUser(user);
        if(user1 != null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(user1);
        }else {
            return ResponseEntity.badRequest().body("Donnés non valid");
        }
    }
}
