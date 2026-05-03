package com.project.HotelBookingWebsite.Strategy;

import com.project.HotelBookingWebsite.entity.Inventory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PricingService {


    public BigDecimal calculateDynamicPricing(Inventory inventory)
    {
        PricingStrategy pricingStrategy=new BasePricingStrategy();


        pricingStrategy=new OccupancyPricingStrategy(pricingStrategy);
        pricingStrategy=new HolidayPricingStrategy(pricingStrategy);
        pricingStrategy=new SurgePricingStrategy(pricingStrategy);
        pricingStrategy=new UrgencyPricingStrategy(pricingStrategy);

        return pricingStrategy.calculatePrice(inventory);

}
//calculate price for whole inventory list
    public BigDecimal calculateTotalPrice(List<Inventory> inventoryList) {
        return inventoryList.stream()
                .map(this::calculateDynamicPricing)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    }
