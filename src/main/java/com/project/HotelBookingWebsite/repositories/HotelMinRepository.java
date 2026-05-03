package com.project.HotelBookingWebsite.repositories;

import com.project.HotelBookingWebsite.dto.HotelPriceDto;
import com.project.HotelBookingWebsite.entity.Hotel;
import com.project.HotelBookingWebsite.entity.HotelMinPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;


public interface HotelMinRepository extends JpaRepository<HotelMinPrice,Long> {

    @Query("""
      SELECT new com.project.HotelBookingWebsite.dto.HotelPriceDto(i.hotel, AVG(i.price))
      FROM HotelMinPrice i
      WHERE i.hotel.city = :city
      AND i.date BETWEEN :startDate AND :endDate
      AND i.hotel.active = true
      AND (:roomCount IS NULL OR 1=1)
      AND (:dateCount IS NULL OR 1=1)
      GROUP BY i.hotel
      """)
    Page<HotelPriceDto> findHotelWithAvailableInventory(
            @Param("city") String city,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomCount") Integer roomCount,
            @Param("dateCount") Long dateCount,
            Pageable pageable
    );


    Optional <HotelMinPrice> findByHotelAndDate(Hotel hotel, LocalDate date);
}
