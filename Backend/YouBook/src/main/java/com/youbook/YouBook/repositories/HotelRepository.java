package com.youbook.YouBook.repositories;

import com.youbook.YouBook.entities.Hotel;
import com.youbook.YouBook.enums.StatusHotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel,Long> {
    List<Hotel> findAll(Specification<Hotel> hotelSpecification);
    Page<Hotel> findAllByStatus(Pageable pageable, StatusHotel status);
    Hotel findByName(String name);
    Hotel findByAddress(String address);
}
