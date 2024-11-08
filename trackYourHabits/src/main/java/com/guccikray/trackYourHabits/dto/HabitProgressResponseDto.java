package com.guccikray.trackYourHabits.dto;

import com.guccikray.trackYourHabits.habitEntity.HabitProgress;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HabitProgressResponseDto {

    private HabitProgress habitProgress;
    private String message;
}
