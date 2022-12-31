package com.youbook.YouBook.services;

import com.youbook.YouBook.entities.Hotel;
import com.youbook.YouBook.entities.Room;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {
    Room addRoom(Hotel hotel,Room room);
    Room updateRoom(Room room);
    Room deleteRoom(Room room);
    List<Room> getAllRooms();
    Room getRoomByNumberAndHotelName(Hotel hotel,int number);
    Room resrveRoom(Hotel hotel, Room room, LocalDate startDate,LocalDate endDate);
    Room cancelResrvation(Room room,LocalDate startDate,LocalDate endDate);
    Room getRoomById(Long id);
}
