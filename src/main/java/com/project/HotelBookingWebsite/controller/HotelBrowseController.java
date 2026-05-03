package com.project.HotelBookingWebsite.controller;


import com.project.HotelBookingWebsite.dto.HotelDto;
import com.project.HotelBookingWebsite.dto.HotelInfoDto;
import com.project.HotelBookingWebsite.dto.HotelPriceDto;
import com.project.HotelBookingWebsite.dto.SearchHotelRequest;
import com.project.HotelBookingWebsite.repositories.HotelRepository;
import com.project.HotelBookingWebsite.repositories.InventoryRepository;
import com.project.HotelBookingWebsite.service.HotelService;
import com.project.HotelBookingWebsite.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.ReactiveOffsetScrollPositionHandlerMethodArgumentResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelBrowseController {

    private final InventoryService inventoryService;
    private final HotelService hotelservice;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelPriceDto>> searchHotels(@RequestBody SearchHotelRequest searchHotelRequest)
    {
   var page=inventoryService.searchHotels(searchHotelRequest);
   return ResponseEntity.ok(page);
    }

    @GetMapping("/{HotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long HotelId)
    {

        return  ResponseEntity.ok(hotelservice.getHotelInfoById(HotelId));
    }

}