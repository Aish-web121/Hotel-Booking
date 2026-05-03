package com.project.HotelBookingWebsite.repositories;

import com.project.HotelBookingWebsite.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface RoomRepository extends JpaRepository<Room,Long> {
}
