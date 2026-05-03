package com.project.HotelBookingWebsite.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  private  Long id;

    @Column(nullable = false)
    private String name;

    private String city;

    @Column(columnDefinition = "TEXT[]")
    private String photos[];

    @Column(columnDefinition = "TEXT[]")
    private String [] amenities;


    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

     @Embedded
    private hotelContactInfo contactInfo;//contact_info_phone_number,contact_info_email and ...
    //here in above camel case gets converted into kebab case which is contactInfoPhoneNumberR--->contact_info_phone_number


    private boolean active;//hotel is active

 @ManyToOne(optional = false)
  private User owner;//one owner can have many hotels


    @OneToMany(mappedBy = "hotel")
            @JsonIgnore
    List<Room> room;//




}
