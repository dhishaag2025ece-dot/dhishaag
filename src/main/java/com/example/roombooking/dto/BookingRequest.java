package com.example.roombooking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalTime;

public record BookingRequest(
        @NotNull(message = "Room is required") @Positive(message = "Invalid room") Long roomId,
        @NotNull(message = "Employee is required") @Positive(message = "Invalid employee") Long employeeId,
        @NotNull(message = "Date is required") LocalDate date,
        @NotNull(message = "Start time is required") LocalTime startTime,
        @NotNull(message = "End time is required") LocalTime endTime) { }