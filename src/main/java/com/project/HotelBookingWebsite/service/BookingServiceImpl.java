package com.project.HotelBookingWebsite.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.HotelBookingWebsite.Exception.ResourceNotFoundException;
import com.project.HotelBookingWebsite.Exception.UnauthorizedException;
import com.project.HotelBookingWebsite.Strategy.PricingService;
import com.project.HotelBookingWebsite.dto.BookingDto;
import com.project.HotelBookingWebsite.dto.BookingRequest;
import com.project.HotelBookingWebsite.dto.GuestDto;
import com.project.HotelBookingWebsite.entity.*;
import com.project.HotelBookingWebsite.entity.enums.BookingStatus;
import com.project.HotelBookingWebsite.repositories.*;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.stripe.model.checkout.Session;
import com.stripe.net.ApiResource;
import com.stripe.model.checkout.Session;
import com.stripe.model.EventDataObjectDeserializer;


import java.awt.print.Book;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService{


    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
  private final RoomRepository roomRepository;
  private final InventoryRepository inventoryRepository;
  private final GuestRepository guestRepository;
  private final ModelMapper mapper;
  private final CheckOutService checkOutService;
  private final PricingService pricingService;

  @Value("${frontend.url}")
private String frontendUrl;

    @Override
    @Transactional
    public BookingDto initialiseBooking(BookingRequest bookingRequest) {
        log.info("initialising booking for hotel {} ,room {},date {}-{}",
                bookingRequest.getHotelId(),bookingRequest.getRoomId(),bookingRequest.getCheckInDate(),bookingRequest.getCheckOutDate());

   Hotel hotel= hotelRepository.findById(bookingRequest.getHotelId()
   ).orElseThrow(()->new ResourceNotFoundException("not found hotel with id"+bookingRequest.getHotelId()));

   Room room= roomRepository.findById(bookingRequest.getRoomId()
   ).orElseThrow(()->new ResourceNotFoundException("not found hotel with id"+bookingRequest.getRoomId()));

   List<Inventory> inventoryList= inventoryRepository.findAndLockAvailableInventory(bookingRequest.getRoomId(),
           bookingRequest.getCheckInDate(),bookingRequest.getCheckOutDate(),bookingRequest.getRoomsCount());

   long daysCount= ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(),bookingRequest.getCheckOutDate())+1;
   if(inventoryList.size()!=daysCount)
   {
       throw new IllegalStateException( "room not available anymore");
   }


  inventoryRepository.initBooking(room.getId(),bookingRequest.getCheckInDate(),
          bookingRequest.getCheckOutDate(),bookingRequest.getRoomsCount());


        BigDecimal priceForOneRoom = pricingService.calculateTotalPrice(inventoryList);
        BigDecimal totalPrice = priceForOneRoom.multiply(BigDecimal.valueOf(bookingRequest.getRoomsCount()));

        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkInDate(bookingRequest.getCheckInDate())
                .checkOutDate(bookingRequest.getCheckOutDate())
                .user(getCurrentUser())
                .roomCount(bookingRequest.getRoomsCount())
                .amount(totalPrice)
                .build();



       return mapper.map(bookingRepository.save(booking), BookingDto.class);
    }

    @Override
    @Transactional
    public BookingDto addGuests(Long bookingId,List<GuestDto> guestDtoList) {

   log.info("adding guests for booking with id {}",bookingId);

        Booking booking= bookingRepository.findById(bookingId
        ).orElseThrow(()->new ResourceNotFoundException("not found hotel with id"+bookingId));

        User user=getCurrentUser();
        if(!user.equals(booking.getUser())){
            throw new UnauthorizedException("booking does not belong to this user"+user.getId());
        }
      log.info("logged in user {}",user.getName());
        log.info("booking user {}",booking.getUser().getName());

        if(bookingHasExpired(booking))
        {
            throw new IllegalStateException("Booking is not under under reserved state,cannot add guest");

        }
        for(GuestDto guestDto: guestDtoList)
        {
            Guest guest=mapper.map(guestDto, Guest.class);
            guest.setUser(user);
            guest=guestRepository.save(guest);
            booking.getGuest().add(guest);
        }
        booking.setBookingStatus(BookingStatus.GUEST_ADDED);
        booking=bookingRepository.save(booking);
        return mapper.map(booking,BookingDto.class);

    }

    @Override
    @Transactional
    public String initiatePayment(Long bookingId)
    {
        Booking booking=bookingRepository.findById(bookingId).orElseThrow(
                ()->new ResourceNotFoundException("not found with id"+bookingId));
 User user=getCurrentUser();
        if(!user.equals(booking.getUser())){
            throw new UnauthorizedException("booking does not belong to this user"+user.getId());
        }

        if(bookingHasExpired(booking))
        {
            throw new IllegalStateException("Booking is not under under reserved state,cannot add guest");

        }
        String sessionUrl= null;
        try {
            sessionUrl = checkOutService.getCheckOutSession
                    (booking,frontendUrl+"/payments/Success",frontendUrl+"/payments/failure");
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
        booking.setBookingStatus(BookingStatus.PAYMENT_PENDING);
        bookingRepository.save(booking);

return sessionUrl;
    }




    @Override
    @Transactional
    public void capturePayment(Event event) {

        if("checkout.session.completed".equals(event.getType())){


//            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
            Session session = retrieveSessionFromEvent(event);
            if(session == null|| session.getId()==null)return;

            String sessionId = session.getId();
            Booking booking = bookingRepository.findByPaymentSessionId(sessionId).orElseThrow(
                    ()-> new ResourceNotFoundException("Booking not found for session Id :"+sessionId)
            );

            booking.setBookingStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);

            inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(),booking.getCheckInDate()
                    ,booking.getCheckOutDate(),booking.getRoomCount());
            inventoryRepository.confirmBooking(booking.getRoom().getId(),booking.getCheckInDate()
                    ,booking.getCheckOutDate(),booking.getRoomCount());

            log.info("Successfully confirmed the booking for Booking Id : {}",booking.getId());
        }
        else {
            log.warn("Unhandled event Type : {}",event.getType());
        }

    }
    @Override
    @org.springframework.transaction.annotation.Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new ResourceNotFoundException("Booking not found with id: "+bookingId)
        );
        User user = getCurrentUser();
        if (!user.equals(booking.getUser())) {
            throw new UnauthorizedException("Booking does not belong to this user with id: "+user.getId());
        }

        if(booking.getBookingStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Only confirmed bookings can be cancelled");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(), booking.getCheckInDate(),
                booking.getCheckOutDate(), booking.getRoomCount());

        inventoryRepository.cancelBooking(booking.getRoom().getId(), booking.getCheckInDate(),
                booking.getCheckOutDate(), booking.getRoomCount());

        // handle the refund

        try {
            Session session = Session.retrieve(booking.getPaymentSessionId());
            RefundCreateParams refundParams = RefundCreateParams.builder()
                    .setPaymentIntent(session.getPaymentIntent())
                    .build();

            Refund.create(refundParams);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBookingStatus(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new ResourceNotFoundException("Booking not found with id: "+bookingId)
        );
        User user = getCurrentUser();
        if (!user.equals(booking.getUser())) {
            throw new UnauthorizedException("Booking does not belong to this user with id: "+user.getId());
        }

        return booking.getBookingStatus().name();
    }


    private Session retrieveSessionFromEvent(Event event) {
        log.info("inside  retrieveSessionFromEvent");
        try {

            EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
            if (deserializer.getObject().isPresent()) {
                return (Session) deserializer.getObject().get();
            } else {
                String rawJson = event.getData().getObject().toJson();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(rawJson);
                String sessionId = jsonNode.get("id").asText();

                return Session.retrieve(sessionId);
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException("Failed to retrieve session data");
        }
    }


    public boolean bookingHasExpired(Booking booking)
    {
      return booking.getCreatedDate().plusMinutes(10L).isBefore(LocalDateTime.now());
    }



    public User getCurrentUser()
    {

        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
