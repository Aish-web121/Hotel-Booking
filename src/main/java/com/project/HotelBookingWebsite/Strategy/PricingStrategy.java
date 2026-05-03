package com.project.HotelBookingWebsite.Strategy;

import com.project.HotelBookingWebsite.entity.Inventory;

import java.math.BigDecimal;

public interface PricingStrategy {

    BigDecimal calculatePrice(Inventory inventory);
}
