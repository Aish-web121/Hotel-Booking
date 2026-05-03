package com.project.HotelBookingWebsite.entity;


import com.project.HotelBookingWebsite.entity.enums.BookingStatus;
import com.project.HotelBookingWebsite.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="")
    private Hotel hotel;

    @Column(nullable = false,precision = 10,scale=2)
    private BigDecimal amount;

    @Column(nullable=false,columnDefinition = "INTEGER DEFAULT 0")
    private Integer roomCount;

    @ManyToOne(fetch=FetchType.LAZY)
    private Room room;

    @ManyToOne(fetch=FetchType.EAGER)
    private User user;

    @Column(nullable = false)
    private LocalDate checkInDate;

    @Column(nullable = false)
    private LocalDate checkOutDate;

   @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updateTime;





    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus bookingStatus;

    @ManyToMany(fetch=FetchType.LAZY)  //many guest-->one booking and many bookings--> one guest
    @JoinTable(name="booking_guests",
            joinColumns = @JoinColumn(name="booking_id"),
            inverseJoinColumns = @JoinColumn(name="guest_id"))
    private Set<Guest> guest;

   @Column(unique = true)
    private String paymentSessionId;




}

