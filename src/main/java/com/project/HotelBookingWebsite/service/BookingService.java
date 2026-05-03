package com.project.HotelBookingWebsite.service;

import com.project.HotelBookingWebsite.dto.BookingDto;
import com.project.HotelBookingWebsite.dto.BookingRequest;
import com.project.HotelBookingWebsite.dto.GuestDto;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;


import java.util.List;

public interface BookingService {
     BookingDto initialiseBooking(BookingRequest bookingRequest);

      BookingDto addGuests(Long bookingId,List<GuestDto> guestDtoList);

    String initiatePayment(Long bookingId) ;

    void capturePayment(Event event);

    void cancelBooking(Long bookingId);

    String getBookingStatus(Long bookingId);
}
