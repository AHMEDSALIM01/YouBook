package com.youbook.YouBook.repositories;

import com.youbook.YouBook.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@CrossOrigin("**")
public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    Reservation getReservationByRef(String ref);
}
