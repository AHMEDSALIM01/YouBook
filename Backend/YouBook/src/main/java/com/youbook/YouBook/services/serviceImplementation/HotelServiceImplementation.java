package com.youbook.YouBook.services.serviceImplementation;

import com.youbook.YouBook.criteria.FilterCriteria;
import com.youbook.YouBook.entities.Hotel;
import com.youbook.YouBook.entities.Room;
import com.youbook.YouBook.enums.StatusHotel;
import com.youbook.YouBook.repositories.HotelRepository;
import com.youbook.YouBook.services.HotelService;
import com.youbook.YouBook.validation.HotelValidatore;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.parameters.P;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HotelServiceImplementation implements HotelService {
    private  HotelRepository hotelRepository;
    private  HotelValidatore hotelValidatore;
    public HotelServiceImplementation(HotelRepository hotelRepository){
        this.hotelRepository = hotelRepository;
    }
    public HotelServiceImplementation(HotelValidatore hotelValidatore){
        this.hotelValidatore = hotelValidatore;
    }

    @Override
    public Hotel addHotel(Hotel hotel) {
        Boolean isValideData = hotelValidatore.validate(hotel);
        if(isValideData){
            if(isExiste(hotel)){
                throw new IllegalStateException("hotel existe déja");
            }
            else {
                return hotelRepository.save(hotel);
            }
        }else {
            throw new IllegalStateException(hotelValidatore.getErrorMessage());
        }
    }

    @Override
    public Hotel updateHotel(Hotel hotel) {
        return null;
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
    public Boolean isAvailaible(LocalDate startDate, LocalDate endDate) {

        return null;
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
        Boolean existe = hotelRepository.findByName(hotel.getName());
        return existe;
    }
}
