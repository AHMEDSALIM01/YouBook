package com.youbook.YouBook.repositories;

import com.youbook.YouBook.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel,Long> {
}
