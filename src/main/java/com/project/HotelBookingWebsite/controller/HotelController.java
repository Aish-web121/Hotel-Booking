package com.project.HotelBookingWebsite.controller;

import com.project.HotelBookingWebsite.dto.HotelDto;
import com.project.HotelBookingWebsite.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelController {

    private final HotelService hotelService;


    @PostMapping
    public ResponseEntity<HotelDto> createNewHotel(@RequestBody HotelDto dto)
    {
        HotelDto hotel=hotelService.createNewHotel(dto);
        return new ResponseEntity<>(hotel, HttpStatus.CREATED);
    }


    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelDto> findById(@PathVariable Long hotelId)
    {
        HotelDto hotelDto=hotelService.findById(hotelId);
        return ResponseEntity.ok(hotelDto);
    }
    @PutMapping("/{hotelId}")
    public ResponseEntity<HotelDto> updateById(@PathVariable Long hotelId,@RequestBody HotelDto dto)
    {
        HotelDto hotelDto=hotelService.updateHotelById(hotelId,dto);
        return ResponseEntity.ok(hotelDto);
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<HotelDto> deleteById(@PathVariable Long hotelId)
    {
        hotelService.deleteById(hotelId);
       return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{hotelId}/activate")
    public ResponseEntity<HotelDto> activateHotel(@PathVariable Long hotelId)
    {
        hotelService.activateHotel(hotelId);
        return ResponseEntity.noContent().build();
    }
}
