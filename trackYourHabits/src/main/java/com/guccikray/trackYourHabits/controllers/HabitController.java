package com.guccikray.trackYourHabits.controllers;

import com.guccikray.trackYourHabits.dto.HabitResponseDto;
import com.guccikray.trackYourHabits.dto.HabitNameDto;
import com.guccikray.trackYourHabits.exceptions.HabitAlreadyExistsException;
import com.guccikray.trackYourHabits.exceptions.HabitNotFoundException;
import com.guccikray.trackYourHabits.habitEntity.Habit;
import com.guccikray.trackYourHabits.jwt.JwtUtil;
import com.guccikray.trackYourHabits.services.HabitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HabitController {

    private final HabitService habitService;
    private final JwtUtil jwtUtil;

    @Autowired
    public HabitController(HabitService habitService, JwtUtil jwtUtil) {
        this.habitService = habitService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/habits")
    public ResponseEntity<HabitResponseDto> createNewHabit(
            @Valid @RequestBody Habit habit,
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            String token = authHeader.substring(7);
            String userId = jwtUtil.extractUserId(token);

            return ResponseEntity.ok(new HabitResponseDto(habitService.createHabit(habit, userId),
                    "Habit created successfully"));
        } catch (HabitAlreadyExistsException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new HabitResponseDto(null, e.getMessage()));
        }
    }

    @GetMapping("/habits")
    public ResponseEntity<List<Habit>> getUserHabits(@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(habitService.getAllUserHabits(userId));

    }

    @PatchMapping("/habits")
    public ResponseEntity<HabitResponseDto> editUserHabit(
            @Valid @RequestBody Habit habit,
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            String token = authHeader.substring(7);
            String userId = jwtUtil.extractUserId(token);

            return ResponseEntity.ok(new HabitResponseDto(habitService.editHabit(habit, userId),
                    "Habit updated successfully"));
        } catch (HabitNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new HabitResponseDto(null, e.getMessage()));
        }
    }

    @DeleteMapping("/habits")
    public ResponseEntity<HabitNameDto> removeHabit(
            @Valid @RequestBody HabitNameDto habitToRemove,
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            String token = authHeader.substring(7);
            String userId = jwtUtil.extractUserId(token);
            habitService.removeHabit(habitToRemove.getName(), userId);
            return ResponseEntity.ok(new HabitNameDto(null, "Habit removed successfully"));
        } catch (HabitNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new HabitNameDto(null, e.getMessage()));
        }
    }
}
