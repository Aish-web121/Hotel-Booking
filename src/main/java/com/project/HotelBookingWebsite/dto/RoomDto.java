package com.project.HotelBookingWebsite.dto;

import com.project.HotelBookingWebsite.entity.Hotel;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {



    private Long id;



    private Hotel hotel;


    private String type;


    private BigDecimal basePrice;


    private Integer totalCount;


    private String photos[];


    private String [] amenities;



    private LocalDateTime createdDate;


    private LocalDateTime updatedDate;


    private Integer capacity;
}
