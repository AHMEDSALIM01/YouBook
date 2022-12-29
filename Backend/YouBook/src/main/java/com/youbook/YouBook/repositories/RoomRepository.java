package com.youbook.YouBook.repositories;

import com.youbook.YouBook.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Long> {
}
