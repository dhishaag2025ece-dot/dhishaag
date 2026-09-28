package com.example.roombooking.controller;

import com.example.roombooking.entity.Room;
import com.example.roombooking.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Feature 1: list rooms with capacity, amenities and availability now
    @GetMapping
    public ResponseEntity<List<Room>> listRooms() {
        return ResponseEntity.ok(roomService.listRooms());
    }

    // Admin only: pass admin's id in header  X-Admin-Id
    @PostMapping
    public ResponseEntity<Room> addRoom(
            @RequestHeader(value = "X-Admin-Id", required = false) Long adminId,
            @RequestBody Room room) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.addRoom(adminId, room));
    }
}
