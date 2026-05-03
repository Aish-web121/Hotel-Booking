package com.project.HotelBookingWebsite.service;

import com.project.HotelBookingWebsite.dto.HotelDto;
import com.project.HotelBookingWebsite.dto.HotelPriceDto;
import com.project.HotelBookingWebsite.dto.SearchHotelRequest;
import com.project.HotelBookingWebsite.entity.Inventory;
import com.project.HotelBookingWebsite.entity.Room;
import com.project.HotelBookingWebsite.repositories.HotelMinRepository;
import com.project.HotelBookingWebsite.repositories.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService{

    private final InventoryRepository inventoryRepository;
    private final ModelMapper mapper;
    private final HotelMinRepository hotelMinRepository;

    @Override
    public void initialializeRoomForYear(Room room) {
        LocalDate today=LocalDate.now();
        LocalDate endDate=today.plusYears(1);
        for(; !today.isAfter(endDate);today=today.plusDays(1))
        {
        Inventory inventory=    Inventory.builder()
                    .city(room.getHotel().getCity())
                    .hotel(room.getHotel())
                    .surge(BigDecimal.ONE)
                    .bookedCount(0)
                .reservedCount(0)
                    .date(today)
                    .room(room)
                    .closed(false)
                    .totalCount(room.getTotalCount())
                    .price(room.getBasePrice())
                    .build();
            inventoryRepository.save(inventory);
        }

    }

    @Override
    public void deleteAllInventories(Room room) {
     log.info("deleting inventories of room with id {}",room.getId());
        inventoryRepository.deleteByRoom(room);
    }

    @Override
    public Page<HotelPriceDto> searchHotels(SearchHotelRequest searchHotelRequest) {

        log.info("Searching Hotels for {} city,from {},to {}",
                searchHotelRequest.getCity(),searchHotelRequest.getStartDate(),searchHotelRequest.getEndDate());


    Pageable pageable= PageRequest.of(searchHotelRequest.getPage(), searchHotelRequest.getSize());
 Long dateCount= ChronoUnit.DAYS.between(searchHotelRequest.getEndDate(),searchHotelRequest.getStartDate())+1;
  Page<HotelPriceDto> hotel=  hotelMinRepository.findHotelWithAvailableInventory(searchHotelRequest.getCity(),searchHotelRequest.getStartDate()
                                                       ,searchHotelRequest.getEndDate(),searchHotelRequest.getRoomsCount(),
                                                        dateCount,pageable);
  return hotel;
    }
}
