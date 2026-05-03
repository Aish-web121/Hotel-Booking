package com.project.HotelBookingWebsite.service;

import com.project.HotelBookingWebsite.Exception.ResourceNotFoundException;
import com.project.HotelBookingWebsite.Exception.UnauthorizedException;
import com.project.HotelBookingWebsite.dto.RoomDto;
import com.project.HotelBookingWebsite.entity.Hotel;
import com.project.HotelBookingWebsite.entity.Room;
import com.project.HotelBookingWebsite.entity.User;
import com.project.HotelBookingWebsite.repositories.HotelRepository;
import com.project.HotelBookingWebsite.repositories.InventoryRepository;
import com.project.HotelBookingWebsite.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService{
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final ModelMapper mapper;
    private final InventoryService inventoryService;
    @Override
    public RoomDto createNewRoom(Long HotelId,RoomDto roomDto) {
        log.info("creating new room in hotel{}", HotelId);
        Hotel hotel = hotelRepository.findById(HotelId).orElseThrow(
                () -> new ResourceNotFoundException("hotel not found with id"));
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner()))
        {
            throw new UnauthorizedException("This user does not own this hotel with id: "+HotelId);
        }

        Room room;
        room = mapper.map(roomDto, Room.class);
        room.setHotel(hotel);
        room= roomRepository.save(room);
        if(hotel.isActive())
        {
            inventoryService.initialializeRoomForYear(room);
        }



        return mapper.map(room,RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomsInHotel(Long HotelId) {
        log.info("getting all the rooms with hotel id {}",HotelId);
        Hotel hotel = hotelRepository.findById(HotelId).orElseThrow(
                () -> new ResourceNotFoundException("hotel not found with id"+HotelId));
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner()))
        {
            throw new UnauthorizedException("This user does not own this hotel with id: "+HotelId);
        }

        return  hotel.getRoom().
                stream().
                map(element->mapper.map(element,RoomDto.class)).collect(Collectors.toList());
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        log.info("getting the rooms with room id {}",roomId);
      Room room= roomRepository.findById(roomId).orElseThrow(
                () -> new ResourceNotFoundException("room not found with id"+roomId));
      return mapper.map(room,RoomDto.class);
    }

    @Override
    public void deleteRoomById(Long roomId) {
log.info("deleting the room with id {}",roomId);
        Room room= roomRepository.findById(roomId).orElseThrow(
                () -> new ResourceNotFoundException("room not found with id"+roomId));
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(room.getHotel().getOwner()))
        {
            throw new UnauthorizedException("This user does not own this hotel with id: "+roomId);
        }

        roomRepository.deleteById(roomId);

  inventoryService.deleteAllInventories(room);

    }
}
