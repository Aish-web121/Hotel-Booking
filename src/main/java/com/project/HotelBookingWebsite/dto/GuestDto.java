package com.project.HotelBookingWebsite.dto;

import com.project.HotelBookingWebsite.entity.User;
import com.project.HotelBookingWebsite.entity.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class GuestDto {

    private Long id;




    private User user;

    private String name;


    private Gender gender;

    private Integer age;
}
