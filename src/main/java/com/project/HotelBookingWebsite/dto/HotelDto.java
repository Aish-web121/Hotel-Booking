package com.project.HotelBookingWebsite.dto;


import com.project.HotelBookingWebsite.entity.hotelContactInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDto {
    private  Long id;
    private String name;
    private String city;
    private String[] photos;
    private String [] amenities;


    @Embedded
    private hotelContactInfo contactInfo;//contact_info_phone_number,contact_info_email and ...
    //here in above camel case gets converted into kebab case which is contactInfoPhoneNumberR--->contact_info_phone_number


    private boolean active;//hotel is active
}


