package com.project.HotelBookingWebsite.service;

import com.project.HotelBookingWebsite.dto.HotelDto;
import com.project.HotelBookingWebsite.dto.HotelPriceDto;
import com.project.HotelBookingWebsite.dto.SearchHotelRequest;
import com.project.HotelBookingWebsite.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {

    void initialializeRoomForYear(Room room);
    void deleteAllInventories(Room room);

    Page<HotelPriceDto> searchHotels(SearchHotelRequest searchHotelRequest);
}
