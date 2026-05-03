package com.project.HotelBookingWebsite.controller;


import com.project.HotelBookingWebsite.dto.RoomDto;
import com.project.HotelBookingWebsite.entity.Room;
import com.project.HotelBookingWebsite.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin/hotels/{HotelId}/rooms")
@RequiredArgsConstructor
public class RoomAdminController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomDto> createNewRoom(@PathVariable Long HotelId,@RequestBody RoomDto roomDto)
    {
       RoomDto room= roomService.createNewRoom(HotelId,roomDto);
        return new ResponseEntity<>(room,HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity <List<RoomDto>> getAllRooms(@PathVariable Long HotelId )
    {
        List<RoomDto> roomDtos=roomService.getAllRoomsInHotel(HotelId);
        return ResponseEntity.ok(roomDtos);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity <RoomDto> getRoomById(@PathVariable Long roomId)
    {
       RoomDto room= roomService.getRoomById(roomId);
        return ResponseEntity.ok(room);
    }

    @DeleteMapping
    public ResponseEntity <RoomDto> DeleteRoomById(@PathVariable Long roomId)
    {
        roomService.deleteRoomById(roomId);
        return ResponseEntity.noContent().build();
    }
}
