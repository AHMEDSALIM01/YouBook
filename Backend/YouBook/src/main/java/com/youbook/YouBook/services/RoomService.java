package com.youbook.YouBook.services;

import com.youbook.YouBook.entities.Hotel;
import com.youbook.YouBook.entities.Reservation;
import com.youbook.YouBook.entities.Room;

import java.util.List;

public interface RoomService {
    Room addRoom(Hotel hotel, Room roomDto);
    Room updateRoom(Room roomDto);
    Room deleteRoom(Room roomDto);
    List<Room> getAllRooms();
    Room getRoomByNumberAndHotelName(Hotel hotel, int number);
    Boolean isRoomAvailable(Reservation reservation);
    Room getRoomById(Long id);
}
