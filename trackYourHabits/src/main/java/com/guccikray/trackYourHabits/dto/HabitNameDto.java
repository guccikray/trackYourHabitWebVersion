package com.guccikray.trackYourHabits.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HabitNameDto {

    private String name;
    private String message;
}
