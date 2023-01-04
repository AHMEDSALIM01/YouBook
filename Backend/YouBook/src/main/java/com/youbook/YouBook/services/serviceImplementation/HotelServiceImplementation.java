package com.youbook.YouBook.services.serviceImplementation;

import com.youbook.YouBook.criteria.FilterCriteria;
import com.youbook.YouBook.entities.Hotel;
import com.youbook.YouBook.entities.Reservation;
import com.youbook.YouBook.entities.Room;
import com.youbook.YouBook.entities.Users;
import com.youbook.YouBook.enums.StatusHotel;
import com.youbook.YouBook.repositories.HotelRepository;
import com.youbook.YouBook.services.HotelService;
import com.youbook.YouBook.services.RoomService;
import com.youbook.YouBook.services.UserService;
import com.youbook.YouBook.validation.HotelValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HotelServiceImplementation implements HotelService {
    private  HotelRepository hotelRepository;
    private RoomService roomService;
    private HotelValidator hotelValidator;
    private UserService userService;
    public HotelServiceImplementation(HotelRepository hotelRepository, RoomService roomService, UserService userService, HotelValidator hotelValidator){
        this.hotelRepository = hotelRepository;
        this.roomService = roomService;
        this.userService = userService;
        this.hotelValidator = hotelValidator;
    }

    @Override
    public Hotel addHotel(Hotel hotel) {
        Boolean isValideData = hotelValidator.validate(hotel);
        if(!isValideData){
            throw new IllegalStateException(hotelValidator.getErrorMessage());
        }
        if(isExiste(hotel)){
            throw new IllegalStateException("hotel existe déja");
        }

        if(hotel.getOwner() == null || hotel.getOwner().getId()==null){
            throw new IllegalStateException("l'hotel doit contenir un propritaire");
        }

        Users owner = userService.getUserById(hotel.getOwner().getId());
        if(owner == null){
            throw new IllegalStateException("le propritaire de cette Hotel n'existe pas");
        }
        hotel.setStatus(StatusHotel.valueOf("En_cours"));
        Hotel hotelSaved = hotelRepository.save(hotel);
        owner.getHotels().add(hotelSaved);
        userService.updateUser(owner.getId(), owner);
        return hotelSaved;
    }

    @Override
    public Hotel updateHotel(Long id,Hotel hotel) {
        Boolean hotelExist = hotelRepository.existsById(id);
        Boolean hotelValide = hotelValidator.validate(hotel);
        if(!hotelExist) {
            throw new IllegalStateException("hotel non trouvé");

        }
        if(!hotelValide){
            throw new IllegalStateException(hotelValidator.getErrorMessage());
        }
        if(hotel.getOwner() == null || hotel.getOwner().getId()==null){
            throw new IllegalStateException("l'hotel doit contenir un propritaire");
        }
        Users owner = userService.getUserById(hotel.getOwner().getId());
        if(owner == null){
            throw new IllegalStateException("le propritaire de cette Hotel n'existe pas");
        }
        Hotel hotelSaved = hotelRepository.save(hotel);
        return hotelRepository.save(hotelSaved);
    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    public Page<Hotel> getAllHotels(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return hotelRepository.findAllByStatus(pageable,StatusHotel.Accépté);
    }

    @Override
    public Hotel delete(Hotel hotel) {
        Boolean hotelExist = hotelRepository.existsById(hotel.getId());
        if(!hotelExist){
            throw new IllegalStateException("Hotel N°" + hotel.getId() + " non trouvé");
        }
        hotelRepository.deleteById(hotel.getId());
        return hotel;
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
                    Join<Room, Reservation> reservations = rooms.join("reservations");
                    predicates.add(cb.not(cb.between(reservations.get("startDate"), criteria.getAvailabilityStart(), criteria.getAvailabilityEnd())));
                    predicates.add(cb.not(cb.between(reservations.get("endDate"), criteria.getAvailabilityStart(), criteria.getAvailabilityEnd())));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
    }

    @Override
    public Hotel accepteHotel(Hotel hotel) {
        Boolean hotelExist = hotelRepository.existsById(hotel.getId());
        if(!hotelExist){
            throw new IllegalStateException("Hotel N°" + hotel.getId() + " non trouvé");
        }

        hotel.setStatus(StatusHotel.valueOf("Accépté"));
        return hotelRepository.save(hotel);
    }

    @Override
    public Hotel refuseHotel(Hotel hotel) {
        Boolean hotelExist = hotelRepository.existsById(hotel.getId());
        if(hotelExist){
            throw new IllegalStateException("Hotel N°" + hotel.getId() + " non trouvé");
        }
        hotel.setStatus(StatusHotel.valueOf("Refusé"));
        return hotelRepository.save(hotel);
    }


    @Override
    public Hotel getById(Long id) {
        Optional<Hotel> hotel = hotelRepository.findById(id);
        if(!hotel.isPresent()){
            throw new IllegalStateException("Hotel non trouvée");
        }
        return hotel.get();

    }
    @Override
    public Boolean isExiste(Hotel hotel) {
        Hotel existe = hotelRepository.findByName(hotel.getName());
        Boolean existById = hotel.getId()!=null ? hotelRepository.existsById(hotel.getId()) : false;
        if(existe!=null || existById){
            return true;
        }
        return false;
    }

    @Override
    public Hotel nonAvailable(Long id, LocalDate startNonAvailable, LocalDate endNonAvailable) {
        Hotel existHotel = hotelRepository.findById(id).orElse(null);
        Boolean isValidDate = hotelValidator.validDate(startNonAvailable,endNonAvailable);
        if(existHotel==null){
            throw new IllegalStateException("Hotel non touvé");
        }
        if(!isValidDate){
            throw new IllegalStateException(hotelValidator.getErrorMessage());
        }
        existHotel.setStartNonAvailable(startNonAvailable);
        existHotel.setEndNonAvailable(endNonAvailable);
        return hotelRepository.save(existHotel);
    }

    @Override
    public Hotel makeHotelAvailable(Long id) {
        Hotel existHotel = hotelRepository.findById(id).orElse(null);
        if(existHotel==null){
            throw new IllegalStateException("Hotel non touvé");
        }
        existHotel.setStartNonAvailable(null);
        existHotel.setEndNonAvailable(null);
        return hotelRepository.save(existHotel);
    }

    @Override
    public Room addRoom(Long id, Room room) {
        Hotel existHotel = hotelRepository.findById(id).orElse(null);
        if(existHotel==null){
            throw new IllegalStateException("Hotel non touvé");
        }
        Room savedRoom = roomService.addRoom(existHotel,room);
        existHotel.getRooms().add(savedRoom);
        if(!existHotel.getRooms().isEmpty() && existHotel.getRooms().size() >= existHotel.getNumberOfRooms()){
            existHotel.setNumberOfRooms(existHotel.getRooms().size());
        }
        hotelRepository.save(existHotel);
        return savedRoom;
    }
}
