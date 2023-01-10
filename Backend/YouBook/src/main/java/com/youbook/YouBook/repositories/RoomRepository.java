package com.youbook.YouBook.repositories;

import com.youbook.YouBook.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@CrossOrigin("**")
public interface RoomRepository extends JpaRepository<Room,Long> {
    Room findByHotelIdAndNumber(Long id, int number);
}
