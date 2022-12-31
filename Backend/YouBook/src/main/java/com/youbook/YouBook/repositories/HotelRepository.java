package com.youbook.YouBook.repositories;

import com.youbook.YouBook.entities.Hotel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel,Long> {
    List<Hotel> findAll(Specification<Hotel> hotelSpecification);
    Hotel findByName(String name);
}
