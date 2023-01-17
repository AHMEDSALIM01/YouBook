package com.youbook.YouBook.services;

import com.youbook.YouBook.criteria.FilterCriteria;
import com.youbook.YouBook.entities.Hotel;
import com.youbook.YouBook.entities.Reservation;
import com.youbook.YouBook.entities.Room;
import com.youbook.YouBook.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface HotelService {
    Hotel addHotel(Hotel hotel);
    Hotel updateHotel(Long id,Hotel hotel);
    List<Hotel> getAllHotels();
    Page<Hotel> getAllHotels(int page,int size);
    Hotel delete(Hotel hotel);
    List<Hotel> filterByCriteria(FilterCriteria criteria);
    Hotel accepteHotel(Hotel hotel);
    Hotel refuseHotel(Hotel hotel);
    Hotel getById(Long id);
    Boolean isExiste(Hotel hotel);
    Hotel nonAvailable(Long id,LocalDate startNonAvailable,LocalDate endNonAvailable);
    Hotel makeHotelAvailable(Long id);
    Room addRoom(Long id,Room room);
    Boolean isHotelAvailable(Reservation reservation);
    List<Hotel> getHotelByOwner(Users owner);
}
