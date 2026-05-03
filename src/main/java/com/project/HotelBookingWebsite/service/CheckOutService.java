package com.project.HotelBookingWebsite.service;

import com.project.HotelBookingWebsite.entity.Booking;
import com.stripe.exception.StripeException;

public interface CheckOutService {


    String getCheckOutSession(Booking booking, String successUrl, String failureUrl) throws StripeException;
}
