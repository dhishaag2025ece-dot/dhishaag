package com.example.roombooking.service;

import com.example.roombooking.dto.BookingRequest;
import com.example.roombooking.entity.Booking;
import com.example.roombooking.entity.Employee;
import com.example.roombooking.entity.Room;
import com.example.roombooking.exception.ApiException;
import com.example.roombooking.repository.BookingRepository;
import com.example.roombooking.repository.EmployeeRepository;
import com.example.roombooking.repository.RoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookings;
    private final RoomRepository rooms;
    private final EmployeeRepository employees;

    public BookingService(BookingRepository bookings, RoomRepository rooms, EmployeeRepository employees) {
        this.bookings = bookings;
        this.rooms = rooms;
        this.employees = employees;
    }

    // Features 2 and 3: book a room, reject overlaps
    public Booking book(BookingRequest r) {
        Room room = rooms.findById(r.roomId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Room not found"));
        Employee employee = employees.findById(r.employeeId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Employee not found"));

        if (!r.endTime().isAfter(r.startTime())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "End time must be after start time");
        }
        if (LocalDateTime.of(r.date(), r.startTime()).isBefore(LocalDateTime.now())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "You cannot book a time in the past");
        }
        // Check conflict BEFORE saving
        if (bookings.countOverlap(room.getId(), r.date(), r.startTime(), r.endTime()) > 0) {
            throw new ApiException(HttpStatus.CONFLICT, "This room is already booked for that time slot");
        }

        Booking b = new Booking();
        b.setRoom(room);
        b.setEmployee(employee);
        b.setBookingDate(r.date());
        b.setStartTime(r.startTime());
        b.setEndTime(r.endTime());
        b.setStatus("CONFIRMED");
        b.setCheckedIn(false);
        return bookings.save(b);
    }

    // Feature 4: cancel (slot is freed because only CONFIRMED bookings block a room)
    public Booking cancel(Long id) {
        Booking b = find(id);
        if (!"CONFIRMED".equals(b.getStatus())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Only confirmed bookings can be cancelled");
        }
        b.setStatus("CANCELLED");
        return bookings.save(b);
    }

    public Booking checkIn(Long id) {
        Booking b = find(id);
        if (!"CONFIRMED".equals(b.getStatus())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "This booking is not active");
        }
        b.setCheckedIn(true);
        return bookings.save(b);
    }

    public List<Booking> myBookings(Long employeeId) {
        return bookings.findByEmployeeIdOrderByBookingDateDescStartTimeDesc(employeeId);
    }

    // Feature 5: every 60 seconds, release bookings with no check-in 10 minutes after start
    @Scheduled(fixedRate = 60000)
    public void releaseNoShows() {
        LocalDateTime now = LocalDateTime.now();
        for (Booking b : bookings.findByStatusAndCheckedInFalse("CONFIRMED")) {
            LocalDateTime deadline = LocalDateTime.of(b.getBookingDate(), b.getStartTime()).plusMinutes(10);
            if (now.isAfter(deadline)) {
                b.setStatus("RELEASED");
                bookings.save(b);
            }
        }
    }

    private Booking find(Long id) {
        return bookings.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Booking not found"));
    }
}
