package com.youbook.YouBook.services.serviceImplementation;

import com.youbook.YouBook.criteria.FilterCriteria;
import com.youbook.YouBook.entities.Hotel;
import com.youbook.YouBook.entities.Reservation;
import com.youbook.YouBook.entities.Room;
import com.youbook.YouBook.enums.StatusHotel;
import com.youbook.YouBook.repositories.HotelRepository;
import com.youbook.YouBook.services.HotelService;
import com.youbook.YouBook.services.RoomService;
import com.youbook.YouBook.validation.HotelValidator;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class HotelServiceImplementation implements HotelService {
    private  HotelRepository hotelRepository;
    private RoomService roomService;
    private HotelValidator hotelValidator;
    public HotelServiceImplementation(HotelRepository hotelRepository,RoomService roomService, HotelValidator hotelValidator){
        this.hotelRepository = hotelRepository;
        this.roomService = roomService;
        this.hotelValidator = hotelValidator;
    }

    @Override
    public Hotel addHotel(Hotel hotel) {
        Boolean isValideData = hotelValidator.validate(hotel);
        if(isValideData){
            if(isExiste(hotel)){
                throw new IllegalStateException("hotel existe déja");
            }
            else {
                hotel.setStatus(StatusHotel.En_cours);
                return hotelRepository.save(hotel);
            }
        }else {
            throw new IllegalStateException(hotelValidator.getErrorMessage());
        }
    }

    @Override
    public Hotel updateHotel(Long id,Hotel hotel) {
        Boolean hotelExist = hotelRepository.existsById(id);
        Boolean hotelValide = hotelValidator.validate(hotel);
        if(hotelExist){
            if(hotelValide){
                return hotelRepository.save(hotel);
            }else{
                throw new IllegalStateException(hotelValidator.getErrorMessage());
            }
        }else{
            throw new IllegalStateException("hotel non trouvé");
        }
    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    public Hotel delete(Hotel hotel) {
        Boolean hotelExist = hotelRepository.existsById(hotel.getId());
        if(hotelExist){
            hotelRepository.deleteById(hotel.getId());
            return hotel;
        }else{
            throw new IllegalStateException("Hotel N°" + hotel.getId() + " non trouvé");
        }
    }

    @Override
    public Boolean isHotelAvailable(Reservation reservation) {
        if(reservation.getRoom()==null || reservation.getStartDate()==null || reservation.getEndDate()==null){
            throw new IllegalStateException("les donnés de réservation est invalids");
        }
        Room room = roomService.getRoomById(reservation.getRoom().getId());
        if(room==null || room.getHotel()==null){
            throw new IllegalStateException("cette chambre n'appartient à aucun hotel");
        }
        Hotel hotel = this.getById(room.getHotel().getId());
        if(hotel.getEndNonAvailable()==null || hotel.getStartNonAvailable()==null){
            return true;
        }

        if (reservation.getStartDate().isBefore(hotel.getEndNonAvailable()) && reservation.getEndDate().isAfter(hotel.getStartNonAvailable())) {
            return false;
        }
            return true;

    }

    @Override
    public List<Hotel> filterByCriteria(FilterCriteria criteria) {
        return hotelRepository.findAll(new Specification<Hotel>(){
            @Override
            public Predicate toPredicate(Root<Hotel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(criteria.getCity()!=null){
                    predicates.add(cb.equal(root.get("city"),criteria.getCity()));
                }
                if(criteria.getHotelName()!=null){
                    predicates.add(cb.like(root.get("name"), "%" + criteria.getHotelName() + "%"));
                }
                if(criteria.getPrixMin()!=null && criteria.getPrixMax()!=null){
                    Join<Hotel, Room> rooms = root.join("rooms");
                    predicates.add(cb.between(rooms.get("price"),criteria.getPrixMin(), criteria.getPrixMin()));
                }if (criteria.getAvailabilityStart() != null && criteria.getAvailabilityEnd() != null){
                    Join<Hotel, Room> rooms = root.join("rooms");
                    predicates.add(cb.between(rooms.get("availability"),criteria.getAvailabilityStart(),criteria.getAvailabilityEnd()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
    }

    @Override
    public Hotel accepteHotel(Hotel hotel) {
        Boolean hotelExist = hotelRepository.existsById(hotel.getId());
        if(hotelExist){
            hotel.setStatus(StatusHotel.Accépté);
            return hotelRepository.save(hotel);
        }
        else {
            throw new IllegalStateException("Hotel N°" + hotel.getId() + " non trouvé");
        }
    }

    @Override
    public Hotel refuseHotel(Hotel hotel) {
        Boolean hotelExist = hotelRepository.existsById(hotel.getId());
        if(hotelExist){
            hotel.setStatus(StatusHotel.Refusé);
            return hotelRepository.save(hotel);
        }
        else {
            throw new IllegalStateException("Hotel N°" + hotel.getId() + " non trouvé");
        }
    }


    @Override
    public Hotel getById(Long id) {
        Optional<Hotel> hotel = hotelRepository.findById(id);
        if(hotel.isPresent()){
            return hotel.get();
        }else {
            throw new IllegalStateException("Hotel non trouvée");
        }

    }
    @Override
    public Boolean isExiste(Hotel hotel) {
        Hotel existe = hotelRepository.findByName(hotel.getName());
        Boolean existById = hotelRepository.existsById(hotel.getId());
        if(existe!=null || existById){
            return true;
        }
        return false;
    }

    @Override
    public Hotel nonAvailable(Long id, LocalDate startNonAvailable, LocalDate endNonAvailable) {
        return null;
    }

    @Override
    public Room addRoom(Long id, Room room) {
        Hotel existHotel = hotelRepository.findById(id).orElse(null);
        if(existHotel!=null){
            Room savedRoom = roomService.addRoom(existHotel,room);
            existHotel.getRooms().add(savedRoom);
            hotelRepository.save(existHotel);
            return savedRoom;
        }else{
            throw new IllegalStateException("Hotel non touvé");
        }
    }
}
