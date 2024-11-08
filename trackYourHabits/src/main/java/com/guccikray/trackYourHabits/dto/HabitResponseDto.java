package com.guccikray.trackYourHabits.dto;

import com.guccikray.trackYourHabits.habitEntity.Habit;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HabitResponseDto {
    private Habit habit;
    private String message;
}
