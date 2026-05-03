package com.project.HotelBookingWebsite.dto;

import com.project.HotelBookingWebsite.entity.Guest;
import com.project.HotelBookingWebsite.entity.Hotel;
import com.project.HotelBookingWebsite.entity.Room;
import com.project.HotelBookingWebsite.entity.User;
import com.project.HotelBookingWebsite.entity.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookingDto {
    private Long id;
    private Integer roomCount;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedTime;
    private BookingStatus bookingStatus;
    private Set<GuestDto> guest;
    private BigDecimal amount;
}
