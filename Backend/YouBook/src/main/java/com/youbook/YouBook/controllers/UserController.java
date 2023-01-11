package com.youbook.YouBook.controllers;

import com.youbook.YouBook.dtos.UserDto;
import com.youbook.YouBook.entities.Users;
import com.youbook.YouBook.services.UserService;
import org.springframework.expression.ExpressionParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

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
    //@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity saveUser(@Validated @RequestBody UserDto user){
        Users user1 = userService.addUser(user);
        if(user1 == null){
            return ResponseEntity.badRequest().body("Donnés non valid");
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(user1);
    }
    @PutMapping("/updateUser/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or #user.email == authentication.name")
    public ResponseEntity updateUser(@PathVariable Long id, @RequestBody Users user){
        Users userCheck = userService.getUserById(id);
        if(!userCheck.getEmail().equals(user.getEmail())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("L'adresse email ne doit pas changer");
        }
        Users userResponse = userService.updateUser(id,user);
        if(userResponse!=null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(userResponse);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Utilisateur n'est pas modifiés");
        }
    }
    @PutMapping("/baneUser/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity baneUser(@PathVariable Long id){
        Users userResponse = userService.bannUser(id);
        if(userResponse!=null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(userResponse);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Utilisateur n'est pas supprimé");
        }
    }
    @PutMapping("/updateRole")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity updateRoleUser(@RequestParam String email,@RequestParam String role){
        Users userResponse = userService.addRoleToUser(email,role);
        if(userResponse!=null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(userResponse);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Utilisateur n'est pas supprimé");
        }
    }
}
