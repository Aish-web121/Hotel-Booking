package com.project.HotelBookingWebsite.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelInfoDto {

    private HotelDto hotel;
    private List<RoomDto> room;
}
