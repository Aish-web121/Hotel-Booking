package com.project.HotelBookingWebsite.service;


import com.project.HotelBookingWebsite.Exception.ResourceNotFoundException;
import com.project.HotelBookingWebsite.Exception.UnauthorizedException;
import com.project.HotelBookingWebsite.dto.HotelDto;
import com.project.HotelBookingWebsite.dto.HotelInfoDto;
import com.project.HotelBookingWebsite.dto.RoomDto;
import com.project.HotelBookingWebsite.entity.Hotel;
import com.project.HotelBookingWebsite.entity.Room;
import com.project.HotelBookingWebsite.entity.User;
import com.project.HotelBookingWebsite.repositories.HotelRepository;
import com.project.HotelBookingWebsite.repositories.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;
   private final ModelMapper mapper;
  private final InventoryService inventoryService;
  private final RoomRepository roomRepository;


    @Override
    public HotelDto createNewHotel(HotelDto hoteldto) {
        log.info("creating new hotel with name: {}",hoteldto.getName());
     Hotel hotel= mapper.map(hoteldto, Hotel.class);
     hotel.setActive(false);
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       hotel.setOwner(user);
       hotel=hotelRepository.save(hotel);
     log.info("created new hotel with id {}",hoteldto.getId());
     return mapper.map(hotel,HotelDto.class);
    }

    @Override
    public HotelDto findById(Long id) {
        log.info("getting hotel with id {}",id);
        Hotel hotel=hotelRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("not found with id"+id));
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner()))
        {
            throw new UnauthorizedException("This user does not own this hotel with id: "+id);
        }

return mapper.map(hotel,HotelDto.class);

    }

    @Override
    public HotelDto updateHotelById(Long id, HotelDto dto) {
        log.info("updating hotel with id {}",id);
        Hotel hotel;
         hotel=hotelRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("not found with id"+id));
      mapper.map(dto,Hotel.class);
        hotel.setId(id);
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner()))
        {
            throw new UnauthorizedException("This user does not own this hotel with id: "+id);
        }
        hotel=hotelRepository.save(hotel);

        return mapper.map(hotel,HotelDto.class);

    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Hotel hotel=hotelRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("not found with id"+id));
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner()))
        {
            throw new UnauthorizedException("This user does not own this hotel with id: "+id);
        }

        for(Room room:hotel.getRoom())
        {
            inventoryService.deleteAllInventories(room);
            roomRepository.deleteById(room.getId());
        }

        hotelRepository.deleteById(id);

    }

    @Override
    @Transactional
    public void activateHotel(Long HotelId) {
        Hotel hotel=hotelRepository.findById(HotelId).orElseThrow(
                ()-> new ResourceNotFoundException("not found with id"+HotelId));
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner()))
        {
            throw new UnauthorizedException("This user does not own this hotel with id: "+HotelId);
        }
        hotel.setActive(true);


        for(Room room:hotel.getRoom())
        {
            inventoryService.initialializeRoomForYear(room);
        }
    }

    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId) {
     Hotel hotel=hotelRepository.findById(hotelId).orElseThrow(
             ()->new ResourceNotFoundException("hotel not found with Id"+hotelId));

        List<RoomDto> room=hotel.getRoom().stream().map(element->mapper.map(element, RoomDto.class))
                .collect(Collectors.toList());

        return new HotelInfoDto(mapper.map(hotel,HotelDto.class),room);
    }


}
