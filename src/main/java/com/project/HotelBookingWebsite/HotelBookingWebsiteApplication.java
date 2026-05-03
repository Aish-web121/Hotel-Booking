package com.project.HotelBookingWebsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HotelBookingWebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelBookingWebsiteApplication.class, args);
	}

}
