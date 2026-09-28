package com.example.roombooking.service;

import com.example.roombooking.entity.Room;
import com.example.roombooking.repository.BookingRepository;
import com.example.roombooking.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository rooms;
    private final BookingRepository bookings;
    private final EmployeeService employeeService;

    public RoomService(RoomRepository rooms, BookingRepository bookings, EmployeeService employeeService) {
        this.rooms = rooms;
        this.bookings = bookings;
        this.employeeService = employeeService;
    }

    // Feature 1: list rooms and whether each is free right now
    public List<Room> listRooms() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        List<Room> list = rooms.findAll();
        for (Room r : list) {
            r.setAvailable(bookings.countOverlap(r.getId(), today, now, now) == 0);
        }
        return list;
    }

    public Room addRoom(Long adminId, Room room) {
        employeeService.requireAdmin(adminId);
        room.setId(null);
        return rooms.save(room);
    }
}