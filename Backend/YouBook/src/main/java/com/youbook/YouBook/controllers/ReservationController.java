package com.youbook.YouBook.controllers;

import com.youbook.YouBook.entities.Reservation;
import com.youbook.YouBook.services.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
    @PostMapping("/addReservation")
    public ResponseEntity saveReservation(@Validated @RequestBody Reservation reservation){
        Reservation reservationResponse = reservationService.addReservation(reservation);
        if(reservationResponse!=null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservationResponse);
        }else{
            return ResponseEntity.badRequest().body("donnés invalides");
        }
    }
    @PutMapping("/cancelReservation")
    public ResponseEntity cancelReservation(@RequestBody Reservation reservation){
        Reservation reservationResponse = reservationService.cancelReservation(reservation);
        if(reservationResponse!=null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservationResponse);
        }else{
            return ResponseEntity.badRequest().body("la resérvation ne peut peux pas annuler");
        }
    }
    @PutMapping("/confirmReservation")
    public ResponseEntity confirmReservation(@RequestBody Reservation reservation){
        Reservation reservationResponse = reservationService.confirmReservation(reservation);
        if(reservationResponse!=null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservationResponse);
        }else{
            return ResponseEntity.badRequest().body("la resérvation ne peut peux pas annuler");
        }
    }
}
