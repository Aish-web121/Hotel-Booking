package com.project.HotelBookingWebsite.Strategy;

import com.project.HotelBookingWebsite.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@RequiredArgsConstructor
public class HolidayPricingStrategy implements PricingStrategy {
    private final PricingStrategy wrapper;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price=wrapper.calculatePrice(inventory);

            boolean isTodayHoliday=true;//api call karenge baad mein

        if(isTodayHoliday)
        {
            return price.multiply(BigDecimal.valueOf(1.25));
        }
        return price;
    }
}
