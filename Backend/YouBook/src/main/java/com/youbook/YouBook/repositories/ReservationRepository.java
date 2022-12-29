package com.youbook.YouBook.repositories;

import com.youbook.YouBook.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
}
