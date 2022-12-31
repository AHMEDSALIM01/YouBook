package com.youbook.YouBook.services.serviceImplementation;

import com.youbook.YouBook.entities.Hotel;
import com.youbook.YouBook.entities.Reservation;
import com.youbook.YouBook.entities.Room;
import com.youbook.YouBook.entities.Users;
import com.youbook.YouBook.enums.StatusReservation;
import com.youbook.YouBook.repositories.RoomRepository;
import com.youbook.YouBook.services.ReservationService;
import com.youbook.YouBook.services.RoomService;
import com.youbook.YouBook.validation.RoomValidator;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class RoomServiceImplementation implements RoomService {
    private RoomRepository roomRepository;
    private RoomValidator roomValidator;
    public RoomServiceImplementation(RoomRepository roomRepository,RoomValidator roomValidator){
        this.roomRepository=roomRepository;
        this.roomValidator = roomValidator;
    }
    @Override
    public Room addRoom(Hotel hotel,Room room) {
        Boolean validateRoom = roomValidator.validate(room);
        if(validateRoom){
            Room room1 = roomRepository.findByHotelIdAndNumber(hotel.getId(),room.getNumber());
            if (room1!=null){
                throw new IllegalStateException("chambre existe déja");
            }else {
                room.setHotel(hotel);
                return roomRepository.save(room);
            }
        }else{
            throw new IllegalStateException(roomValidator.getErrorMessage());
        }
    }

    @Override
    public Room updateRoom(Room room) {
        return null;
    }

    @Override
    public Room deleteRoom(Room room) {
        return null;
    }

    @Override
    public List<Room> getAllRooms() {
        return null;
    }

    @Override
    public Room getRoomByNumberAndHotelName(Hotel hotel, int number) {
        return null;
    }

    @Override
    public Boolean isRoomAvailable(Reservation reservation) {
        LocalDate startDate = reservation.getStartDate();
        LocalDate endDate = reservation.getEndDate();
        Room room = this.getRoomById(reservation.getRoom().getId());
        if(room!=null){
            for (Reservation r : room.getReservations()) {
                if (r!=null && r.getStatus() == StatusReservation.En_cours &&
                        (r.getStartDate().isBefore(endDate) && r.getEndDate().isAfter(startDate))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Room getRoomById(Long id) {
        Optional<Room> room = roomRepository.findById(id);
        if(room.isPresent()){
            return room.get();
        }else {
            throw new IllegalStateException("la chambre non trouvé");
        }
    }
}
