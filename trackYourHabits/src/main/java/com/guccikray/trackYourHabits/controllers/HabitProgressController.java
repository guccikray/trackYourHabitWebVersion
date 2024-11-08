package com.guccikray.trackYourHabits.controllers;

import com.guccikray.trackYourHabits.dto.HabitNameDto;
import com.guccikray.trackYourHabits.dto.HabitProgressResponseDto;
import com.guccikray.trackYourHabits.exceptions.HabitNotFoundException;
import com.guccikray.trackYourHabits.habitEntity.HabitProgress;
import com.guccikray.trackYourHabits.jwt.JwtUtil;
import com.guccikray.trackYourHabits.services.HabitProgressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/habits")
public class HabitProgressController {

    private final HabitProgressService habitProgressService;
    private final JwtUtil jwtUtil;

    @Autowired
    public HabitProgressController(HabitProgressService habitProgressService, JwtUtil jwtUtil) {
        this.habitProgressService = habitProgressService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/habitProgress")
    public ResponseEntity<HabitProgressResponseDto> addHabitProgress (
            @Valid @RequestBody HabitProgress habitProgress,
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            String token = authHeader.substring(7);
            String userId = jwtUtil.extractUserId(token);

            return ResponseEntity.ok(new HabitProgressResponseDto(
                    habitProgressService.addHabitProgress(habitProgress, userId),
                    "Habit created successfully"));
        } catch (HabitNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new HabitProgressResponseDto(null, e.getMessage()));
        }
    }

    @GetMapping("/habitProgress")
    public ResponseEntity<HabitNameDto> getCountOfCompletedDays(
            @Valid @RequestBody HabitNameDto habitNameDto,
            @RequestHeader("Authorization") String authHeader
    ){
        try {
            String token = authHeader.substring(7);
            String userId = jwtUtil.extractUserId(token);

            return ResponseEntity.ok(new HabitNameDto(null,
                    "Count of days when habit is completed " +
                    habitProgressService.countCompletedDays(habitNameDto.getName(), userId) +
                            " of " + habitProgressService.getAllHabitProgress(habitNameDto.getName(), userId).size()));
        } catch (HabitNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new HabitNameDto(null, e.getMessage()));
        }
    }
}
