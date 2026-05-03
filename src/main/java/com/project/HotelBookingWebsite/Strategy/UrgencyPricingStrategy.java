package com.project.HotelBookingWebsite.Strategy;


import com.project.HotelBookingWebsite.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@RequiredArgsConstructor
public class UrgencyPricingStrategy implements PricingStrategy{


    private final PricingStrategy wrapper;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
       BigDecimal price=wrapper.calculatePrice(inventory);
        LocalDate today=LocalDate.now();
        if(!inventory.getDate().isBefore(today)&&inventory.getDate().isBefore(today.plusDays(7)))
        {
return price.multiply(BigDecimal.valueOf(1.15));
        }
        return price;
    }
}
