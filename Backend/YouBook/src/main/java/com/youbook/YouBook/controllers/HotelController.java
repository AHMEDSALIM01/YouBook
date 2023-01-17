package com.youbook.YouBook.controllers;

import com.youbook.YouBook.criteria.FilterCriteria;
import com.youbook.YouBook.entities.Hotel;
import com.youbook.YouBook.entities.Room;
import com.youbook.YouBook.entities.Users;
import com.youbook.YouBook.services.HotelService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotel")
public class HotelController {
    private HotelService hotelService;
    protected HotelController(HotelService hotelService){
        this.hotelService = hotelService;
    }
    @GetMapping("/")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('OWNER')")
    public ResponseEntity<List<Hotel>> getAllHotels(){
        List<Hotel> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/hotels")
    public ResponseEntity<Page<Hotel>> getAllHotels(@RequestParam (defaultValue = "0") int page){
        Page<Hotel> hotels = hotelService.getAllHotels(page, 7);
        System.out.println(hotels);
        return ResponseEntity.ok(hotels);
    }
    @PostMapping("/filter")
    public ResponseEntity getAllHotelsByCriteria(@RequestBody FilterCriteria criteria){
        List<Hotel> hotels = hotelService.filterByCriteria(criteria);
        return ResponseEntity.ok(hotels);
    }
    @PutMapping("/acceptHotel")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('OWNER')")
    public ResponseEntity<Hotel> acceptHotel(@RequestBody Hotel hotel){
        Hotel response = hotelService.accepteHotel(hotel);
        if(response != null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(hotel);
        }
    }
    @PutMapping("/refuseHotel")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('OWNER')")
    public ResponseEntity<Hotel> refuseHotel(@RequestBody Hotel hotel){
        Hotel response = hotelService.refuseHotel(hotel);
        if(response != null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(hotel);
        }
    }
    @PutMapping("/nonAvailable")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('OWNER')")
    public ResponseEntity makeHotelNonAvailable(@RequestBody Hotel hotel){
        Hotel response = hotelService.nonAvailable(hotel.getId(),hotel.getStartNonAvailable(),hotel.getEndNonAvailable());
        if(response!=null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("les donnés de l'hotel est invalid");
        }
    }
    @PutMapping("/available")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('OWNER')")
    public ResponseEntity makeHotelAvailable(@RequestBody Hotel hotel){
        Hotel response = hotelService.makeHotelAvailable(hotel.getId());
        if(response!=null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("les donnés de l'hotel est invalid");
        }
    }
    @PostMapping("/addHotel")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('OWNER')")
    public ResponseEntity addHotel(@Validated @RequestBody Hotel hotel){
        Hotel response = hotelService.addHotel(hotel);
        if(response != null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(hotel);
        }
    }
    @PostMapping("/addRoom/{id}")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('OWNER')")
    public ResponseEntity addRoom(@PathVariable Long id,@Validated @RequestBody Room room){
        Room response = hotelService.addRoom(id, room);
        if(response.equals(room)){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity getHotel(@PathVariable Long id){
        Hotel hotel = hotelService.getById(id);
        if(hotel!=null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(hotel);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hotel N° "+id+"n'existe pas");
        }
    }

    @DeleteMapping("/deleteHotel")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('OWNER')")
    public ResponseEntity deletHotel(@RequestBody Hotel hotel){
        Hotel hotelresponse = hotelService.delete(hotel);
        try {
            if(hotel!=null){
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(hotelresponse);
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hotel n'est pas supprimer");
            }

        }catch (IllegalStateException e){
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/updateHotel")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('OWNER')")
    public ResponseEntity updateHotel(@RequestBody Hotel hotel){
        Hotel hotelresponse = hotelService.updateHotel(hotel.getId(),hotel);
        try {
            if(hotel!=null){
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(hotelresponse);
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hotel n'est pas modifié");
            }

        }catch (IllegalStateException e){
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
    @PostMapping("/hotelOwner")
    public ResponseEntity getHotelByOwner(@RequestBody Users owner){
        try{
            List<Hotel> hotels=hotelService.getHotelByOwner(owner);
            return ResponseEntity.status(200).body(hotels);
        }catch (IllegalStateException e){
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
