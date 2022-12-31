package com.youbook.YouBook.services;

import com.youbook.YouBook.criteria.FilterCriteria;
import com.youbook.YouBook.entities.Hotel;
import com.youbook.YouBook.entities.Room;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public interface HotelService {
    Hotel addHotel(Hotel hotel);
    Hotel updateHotel(Long id,Hotel hotel);
    List<Hotel> getAllHotels();
    Hotel delete(Hotel hotel);
    Boolean isAvailable(Hotel hotel, Room room, LocalDate startDate, LocalDate endDate);
    List<Hotel> filterByCriteria(FilterCriteria criteria);
    Hotel accepteHotel(Hotel hotel);
    Hotel refuseHotel(Hotel hotel);
    Hotel getById(Long id);
    Boolean isExiste(Hotel hotel);
    Hotel nonAvailable(Long id,LocalDate startNonAvailable,LocalDate endNonAvailable);
    Room addRoom(Long id,Room room);
}
