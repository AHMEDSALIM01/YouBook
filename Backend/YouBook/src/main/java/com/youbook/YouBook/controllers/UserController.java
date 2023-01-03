package com.youbook.YouBook.controllers;

import com.youbook.YouBook.entities.Users;
import com.youbook.YouBook.repositories.UserRepository;
import com.youbook.YouBook.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    @PostAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity getAllUsers(){
        List<Users> usersList = userService.getAllUsers();
        return ResponseEntity.ok(usersList);
    }
    @PostMapping("/addUser")
    @PostAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity saveUser(@Validated @RequestBody Users user){
        Users user1 = userService.addUser(user);
        if(user1 != null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(user1);
        }else {
            return ResponseEntity.badRequest().body("Donnés non valid");
        }
    }
    @PutMapping("/updateUser/{id}")
    @PostAuthorize("#user.email = authentication.name or hasAuthority('ADMIN')")
    public ResponseEntity updateUser(@PathVariable Long id,@RequestBody Users user){
        Users userResponse = userService.updateUser(id,user);
        if(userResponse!=null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(userResponse);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Utilisateur n'est pas modifiés");
        }
    }
    @PutMapping("/{id}")
    @PostAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity baneUser(@PathVariable Long id){
        Users userResponse = userService.bannUser(id);
        if(userResponse!=null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(userResponse);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Utilisateur n'est pas supprimé");
        }
    }
}
