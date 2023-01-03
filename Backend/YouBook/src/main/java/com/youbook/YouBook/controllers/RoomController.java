package com.youbook.YouBook.controllers;

import com.youbook.YouBook.entities.Room;
import com.youbook.YouBook.services.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {
    private RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Room>> getAllRooms(){
        List<Room> rooms = roomService.getAllRooms();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(rooms);
    }
    @GetMapping("/{id}")
    public ResponseEntity getRoomById(@PathVariable Long id){
        Room room = roomService.getRoomById(id);
        if(room == null){
            return ResponseEntity.badRequest().body("chambre non trouvé");
        }
        return ResponseEntity.ok().body(room);
    }
    @PutMapping("/updateRoom")
    public ResponseEntity updateRoom(@RequestBody Room room){
        Room roomResponse = roomService.updateRoom(room);
        if(roomResponse==null){
            return ResponseEntity.badRequest().body("chambre n'est pas modifiée");
        }
        return ResponseEntity.ok().body(room);
    }
    @DeleteMapping("/deleteRoom")
    public ResponseEntity deleteRoom(@RequestBody Room room){
        Room roomResponse = roomService.deleteRoom(room);
        if(roomResponse==null){
            return ResponseEntity.badRequest().body("chambre n'est pas supprimée");
        }
        return ResponseEntity.ok().body(room);
    }

}
