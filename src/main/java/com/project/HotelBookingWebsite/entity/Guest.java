package com.project.HotelBookingWebsite.entity;


import com.project.HotelBookingWebsite.entity.enums.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable = false)
   private String name;

    @Column(nullable = false)
   @Enumerated(EnumType.STRING)
   private Gender gender;

    private Integer age;

//    @ManyToOne
//    @JoinColumn(name = "booking_id") // 🔑 important
//    private Booking booking;
}
