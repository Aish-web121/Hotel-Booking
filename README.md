Hotel Booking Website — Backend
A production-ready backend for a hotel booking platform built with Java Spring Boot. Features JWT authentication, dynamic pricing engine, Stripe payment integration, and real-time inventory management.
Tech Stack
Java 23 + Spring Boot 3.4, PostgreSQL, Spring Security + JWT, Stripe, Hibernate/JPA, Lombok + ModelMapper
Features

JWT Authentication — Secure login & registration
Role-Based Access Control — Guest vs Hotel Manager
Hotel & Room Management — Full CRUD for admins
Real-Time Inventory Tracking — Prevents double bookings
Dynamic Pricing Engine — Prices adjust based on occupancy, holidays, surge demand, and urgency
Stripe Payment Integration — Full payment lifecycle with webhook support
Automatic Refunds on booking cancellation
Global Exception Handling — Structured API error responses

Booking Flow

Initialize Booking → Room reserved, inventory locked
Add Guests → Guest details added
Initiate Payment → Stripe checkout session created
Payment Confirmed → Webhook confirms, booking finalized
Cancel → Inventory released, refund processed

Design Patterns
Strategy Pattern, Builder Pattern, Repository Pattern
Author
Aishwarya Ranjan
GitHub: https://github.com/Aish-web121
