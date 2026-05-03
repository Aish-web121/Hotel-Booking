package com.project.HotelBookingWebsite.service;


import com.project.HotelBookingWebsite.entity.Booking;
import com.project.HotelBookingWebsite.entity.User;
import com.project.HotelBookingWebsite.repositories.BookingRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckOutServiceImpl implements CheckOutService{

 private final BookingRepository bookingRepository;

    public String getCheckOutSession(Booking booking, String successUrl, String failureUrl) throws StripeException {
        log.info("Creating  Session with Id {}",booking.getId());
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CustomerCreateParams CustomerParams= CustomerCreateParams.builder().
                setEmail(user.getEmail())
                .setName(user.getName())
                .build();
        Customer customer=Customer.create(CustomerParams);


        SessionCreateParams sessionCreateParams= SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                .setCustomer(customer.getId())
                .setSuccessUrl(successUrl)
                .setCancelUrl(failureUrl)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("inr")
                                                .setUnitAmount(booking.getAmount().multiply(BigDecimal.valueOf(5000)).longValue())
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(booking.getHotel().getName()+"  "+booking.getRoom().getType())
                                                                .setDescription("Booking Id"+booking.getId())
                                                                .build()


                                                )
                                                .build()
                                ).build()


                )
                .build();

        Session session=Session.create(sessionCreateParams);


        booking.setPaymentSessionId(session.getId());
        bookingRepository.save(booking);
        log.info("SessionCreated Successfully for id {}",booking.getId());
        return session.getUrl();

    }
}
