package com.youbook.YouBook.services.serviceImplementation;

import com.youbook.YouBook.entities.Hotel;
import com.youbook.YouBook.entities.Room;
import com.youbook.YouBook.repositories.RoomRepository;
import com.youbook.YouBook.services.HotelService;
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
                room.setAvailability(true);
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
    public Room resrveRoom(Hotel hotel, Room room, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public Room cancelResrvation(Room room, LocalDate startDate, LocalDate endDate) {
        return null;
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
