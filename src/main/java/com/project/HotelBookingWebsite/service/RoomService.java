package com.project.HotelBookingWebsite.service;


import com.project.HotelBookingWebsite.dto.RoomDto;

import java.util.List;

public interface RoomService {

    RoomDto createNewRoom(Long HotelId,RoomDto roomDto);
    List<RoomDto> getAllRoomsInHotel(Long HotelId);
    RoomDto getRoomById(Long roomId);
    void deleteRoomById(Long roomId);

}
