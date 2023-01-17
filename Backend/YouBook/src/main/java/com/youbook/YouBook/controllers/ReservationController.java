package com.youbook.YouBook.controllers;

import com.youbook.YouBook.entities.Reservation;
import com.youbook.YouBook.entities.Users;
import com.youbook.YouBook.services.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("**")
@RequestMapping("/reservation")
public class ReservationController {
    private ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
    @PostMapping("/addReservation")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity saveReservation(@Validated @RequestBody Reservation reservation){
        try{
            Reservation reservationResponse = reservationService.addReservation(reservation);
            if(reservationResponse!=null){
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservationResponse);
            }else{
                return ResponseEntity.badRequest().body("donnés invalides");
            }
        }catch (IllegalStateException e){
            String message = e.getMessage();
            return ResponseEntity.status(401).body(message);
        }
    }
    @PutMapping("/cancelReservation")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity cancelReservation(@RequestBody Reservation reservation){
        try {
            Reservation reservationResponse = reservationService.cancelReservation(reservation);
            if(reservationResponse!=null){
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservationResponse);
            }else{
                throw new IllegalStateException("la resérvation ne peut peux pas annuler");
            }
        }catch (IllegalStateException e){
            return ResponseEntity.status(401).body(e.getMessage());
        }

    }
    @PutMapping("/confirmReservation")
    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity confirmReservation(@RequestBody Reservation reservation){
        Reservation reservationResponse = reservationService.confirmReservation(reservation);
        if(reservationResponse!=null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservationResponse);
        }else{
            return ResponseEntity.badRequest().body("la resérvation ne peut peux pas annuler");
        }
    }
    @PutMapping("/updateReservation/{ref}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity updateReservation(@PathVariable String ref,@RequestBody Reservation reservation){
        try{
            Reservation reservationResponse = reservationService.updateReservation(ref, reservation);
            if(reservationResponse!=null){
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservationResponse);
            }else{
                return ResponseEntity.badRequest().body("la resérvation n'est pax modifier");
            }
        }catch (IllegalStateException e){
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
    @GetMapping("/{ref}")
    public ResponseEntity getReservationByRef(@PathVariable String ref){
        Reservation reservationResponse = reservationService.getReservationByRef(ref);
        if(reservationResponse!=null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservationResponse);
        }else{
            return ResponseEntity.badRequest().body("la resérvation n'est pas trouvée");
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity getReservationById(@PathVariable Long id){
        Reservation reservationResponse = reservationService.getReservationById(id);
        if(reservationResponse!=null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservationResponse);
        }else{
            return ResponseEntity.badRequest().body("la resérvation n'est pas trouvée");
        }
    }

    @PostMapping("/reservations")
    public ResponseEntity getAllByUserId(@RequestBody Users user){
        try {
            List<Reservation> reservationResponse = reservationService.getReservationByUserId(user);
            System.out.println(reservationResponse);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservationResponse);
        }catch (IllegalStateException e){
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity getAllReservations(){
        List<Reservation> reservationResponse = reservationService.getAllReservations();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservationResponse);
    }
}
