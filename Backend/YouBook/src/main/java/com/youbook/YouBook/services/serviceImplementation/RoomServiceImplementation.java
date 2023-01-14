package com.youbook.YouBook.services.serviceImplementation;

import com.youbook.YouBook.entities.Hotel;
import com.youbook.YouBook.entities.Reservation;
import com.youbook.YouBook.entities.Room;
import com.youbook.YouBook.enums.StatusReservation;
import com.youbook.YouBook.repositories.RoomRepository;
import com.youbook.YouBook.services.RoomService;
import com.youbook.YouBook.validation.RoomValidator;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImplementation implements RoomService {
    private RoomRepository roomRepository;
    private RoomValidator roomValidator;
    public RoomServiceImplementation(RoomRepository roomRepository,RoomValidator roomValidator){
        this.roomRepository=roomRepository;
        this.roomValidator = roomValidator;
    }
    @Override
    public Room addRoom(Hotel hotel, Room room) {
        Boolean validateRoom = roomValidator.validate(room);
        if(!validateRoom){
            throw new IllegalStateException(roomValidator.getErrorMessage());
        }
        Room room1 = roomRepository.findByHotelIdAndNumber(hotel.getId(), room.getNumber());
        if (room1 !=null){
            throw new IllegalStateException("chambre existe déja");
        }
        room.setHotel(hotel);
        return roomRepository.save(room);
    }

    @Override
    public Room updateRoom(Room room) {
        Boolean validateRoom = roomValidator.validate(room);
        if(!validateRoom){
            throw new IllegalStateException(roomValidator.getErrorMessage());
        }
        return roomRepository.save(room);
    }

    @Override
    public Room deleteRoom(Room room) {
        if(room.getId()==null){
            throw new IllegalStateException("la chambre doit contenir un Id");
        }
        Room roomExist = this.getRoomById(room.getId());
        if(roomExist ==null){
            throw new IllegalStateException("la chambre n'est pas trouvée");
        }
        roomRepository.deleteById(room.getId());
        return room;
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Boolean isRoomAvailable(Reservation reservation) {
        LocalDate startDate = reservation.getStartDate();
        LocalDate endDate = reservation.getEndDate();
        Room room = this.getRoomById(reservation.getRoom().getId());
        if(room !=null){
            for (Reservation r : room.getReservations()) {
                if ((r!=null && (r.getStatus() == StatusReservation.En_cours || r.getStatus() == StatusReservation.Confirmée) &&
                        (r.getStartDate().isBefore(endDate) && r.getEndDate().isAfter(startDate)))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Room getRoomById(Long id) {
        Optional<Room> room = roomRepository.findById(id);
        if(!room.isPresent()){
            throw new IllegalStateException("la chambre non trouvé");
        }
        return room.get();
    }
}
