package com.project.HotelBookingWebsite.dto;


import com.project.HotelBookingWebsite.entity.Hotel;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelPriceDto {

    private Hotel hotel;
    private double price;
}
