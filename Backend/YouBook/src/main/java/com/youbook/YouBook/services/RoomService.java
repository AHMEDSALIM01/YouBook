package com.youbook.YouBook.services;

import com.youbook.YouBook.entities.Hotel;
import com.youbook.YouBook.entities.Reservation;
import com.youbook.YouBook.entities.Room;
import com.youbook.YouBook.entities.Users;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {
    Room addRoom(Hotel hotel,Room room);
    Room updateRoom(Room room);
    Room deleteRoom(Room room);
    List<Room> getAllRooms();
    Room getRoomByNumberAndHotelName(Hotel hotel,int number);
    Boolean isRoomAvailable(Reservation reservation);
    Room getRoomById(Long id);
}
