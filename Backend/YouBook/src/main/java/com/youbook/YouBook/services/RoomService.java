package com.youbook.YouBook.services;

import com.youbook.YouBook.entities.Hotel;
import com.youbook.YouBook.entities.Reservation;
import com.youbook.YouBook.entities.Room;

import java.util.List;

public interface RoomService {
    Room addRoom(Hotel hotel, Room room);
    Room updateRoom(Room room);
    Room deleteRoom(Room room);
    List<Room> getAllRooms();
    Boolean isRoomAvailable(Reservation reservation);
    Room getRoomById(Long id);
}
