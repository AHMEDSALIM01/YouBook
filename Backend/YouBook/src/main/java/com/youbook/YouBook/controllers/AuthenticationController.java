package com.youbook.YouBook.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youbook.YouBook.entities.Users;
import com.youbook.YouBook.security.filters.JwtAuthenticationFilter;
import com.youbook.YouBook.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("**")
public class AuthenticationController {
    private UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/signUpClient")
    public ResponseEntity signUpClient(@Validated @RequestBody Users user){
        try {
            Users user1 = userService.signUp("CLIENT",user);
            if(user1 != null){
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(user1);
            }else {
                return ResponseEntity.badRequest().body("Donnés non valid");
            }
        }catch (IllegalStateException e){
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
    @PostMapping("/signUpOwner")
    public ResponseEntity signUpOwner(@Validated @RequestBody Users user){
        try {
            Users user1 = userService.signUp("Owner",user);
            if(user1 != null){
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(user1);
            }else {
                return ResponseEntity.badRequest().body("Donnés non valid");
            }
        }catch (IllegalStateException e){
            return ResponseEntity.status(401).body(e.getMessage());
        }

    }



    @GetMapping("/refreshToken")
    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String authToken = request.getHeader("Authorization");
        System.out.println(authToken);
        if(authToken !=null && authToken.startsWith("Bearer ")){
            try {
                String refreshToken = authToken.substring(7);
                Algorithm algorithm = Algorithm.HMAC256("monSecret926600");
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);
                String email = decodedJWT.getSubject();
                Users user = userService.loadUserByEmail(email);
                String jwtAccessToken = JWT.create().withSubject(user.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis()+2*60*1000))
                        .withClaim("roles",user.getRoles().stream().map(r->r.getName()).collect(Collectors.toList()))
                        .withClaim("user_id",user.getId())
                        .withClaim("enabled",user.getIs_active())
                        .withClaim("user_name",user.getName())
                        .sign(algorithm);
                Map<String,String> idToken = new HashMap<>();
                idToken.put("access-token",jwtAccessToken);
                idToken.put("refresh-token",refreshToken);
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(),idToken);
            }
            catch (Exception e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }else{
            return ResponseEntity.status(400).body("Refresh token est obligatoire!!");
        }
        return null;
    }
}
