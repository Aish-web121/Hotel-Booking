package com.project.HotelBookingWebsite.service;

import com.project.HotelBookingWebsite.dto.HotelDto;
import com.project.HotelBookingWebsite.dto.HotelInfoDto;


public interface HotelService {

    HotelDto createNewHotel(HotelDto hoteldto);
    HotelDto findById(Long id);
    HotelDto updateHotelById(Long id,HotelDto dto);
    void deleteById(Long id);
    void activateHotel(Long HotelId);

   HotelInfoDto getHotelInfoById(Long hotelId);
}
