package com.example.roombooking.controller;

import com.example.roombooking.dto.BookingRequest;
import com.example.roombooking.entity.Booking;
import com.example.roombooking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Features 2 and 3: book a room, overlap rejected with 409
    @PostMapping
    public ResponseEntity<Booking> book(@Valid @RequestBody BookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.book(request));
    }

    // Feature 4: cancel booking
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancel(id));
    }

    // Feature 5: check in (otherwise auto-released after 10 minutes)
    @PutMapping("/{id}/check-in")
    public ResponseEntity<Booking> checkIn(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.checkIn(id));
    }

    // My bookings of an employee
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Booking>> myBookings(@PathVariable Long employeeId) {
        return ResponseEntity.ok(bookingService.myBookings(employeeId));
    }
}
