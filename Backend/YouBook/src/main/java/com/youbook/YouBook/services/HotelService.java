package com.youbook.YouBook.services;

import com.youbook.YouBook.criteria.FilterCriteria;
import com.youbook.YouBook.entities.Hotel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public interface HotelService {
    Hotel addHotel(Hotel hotel);
    Hotel updateHotel(Hotel hotel);
    List<Hotel> getAllHotels();
    Hotel delete(Hotel hotel);
    Boolean isAvailaible(LocalDate startDate,LocalDate endDate);
    List<Hotel> filterByCriteria(FilterCriteria criteria);
    Hotel accepteHotel(Hotel hotel);
    Hotel refuseHotel(Hotel hotel);
    Hotel getById(Long id);
    Boolean isExiste(Hotel hotel);
}
