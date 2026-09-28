package com.example.roombooking.repository;

import com.example.roombooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Overlap rule: existingStart < newEnd AND existingEnd > newStart
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.room.id = :roomId AND b.status = 'CONFIRMED' "
            + "AND b.bookingDate = :date AND b.startTime < :end AND b.endTime > :start")
    long countOverlap(@Param("roomId") Long roomId, @Param("date") LocalDate date,
                      @Param("start") LocalTime start, @Param("end") LocalTime end);

    List<Booking> findByEmployeeIdOrderByBookingDateDescStartTimeDesc(Long employeeId);

    List<Booking> findByStatusAndCheckedInFalse(String status);
}