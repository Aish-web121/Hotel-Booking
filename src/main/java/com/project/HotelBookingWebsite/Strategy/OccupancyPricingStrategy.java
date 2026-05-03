package com.project.HotelBookingWebsite.Strategy;

import com.project.HotelBookingWebsite.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@RequiredArgsConstructor
public class OccupancyPricingStrategy implements PricingStrategy{

    private final PricingStrategy wrapper;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {

 double occupancyRate= (double) (inventory.getBookedCount()/ inventory.getTotalCount());
 if(occupancyRate>=0.8)
 {
     return wrapper.calculatePrice(inventory).multiply(BigDecimal.valueOf(1.2));
 }
 return wrapper.calculatePrice(inventory);

    }
}
