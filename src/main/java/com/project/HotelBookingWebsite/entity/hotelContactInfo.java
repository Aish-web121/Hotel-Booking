package com.project.HotelBookingWebsite.entity;


import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class hotelContactInfo {

    private String address;
    private String email;
    private String phoneNumber;
    private String location;



}
