package com.youbook.YouBook.controllers;

import com.youbook.YouBook.entities.Hotel;
import com.youbook.YouBook.entities.Room;
import com.youbook.YouBook.services.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Hotel")
public class HotelController {
    private HotelService hotelService;
    protected HotelController(HotelService hotelService){
        this.hotelService = hotelService;
    }
    @GetMapping("/")
    public List<Hotel> getAllHotels(){
        return hotelService.getAllHotels();
    }
    @PostMapping("/addHotel")
    public ResponseEntity addHotel(@Validated @RequestBody Hotel hotel){
        Hotel response = hotelService.addHotel(hotel);
        if(response != null && response.equals(hotel)){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @PostMapping("/addRoom/{id}")
    public ResponseEntity addRoom(@PathVariable Long id,@Validated @RequestBody Room room){
        Room response = hotelService.addRoom(id,room);
        if(response.equals(room)){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

}
