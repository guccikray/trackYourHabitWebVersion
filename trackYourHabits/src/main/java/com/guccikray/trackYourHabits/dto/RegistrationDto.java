package com.guccikray.trackYourHabits.dto;

import com.guccikray.trackYourHabits.userEntity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationDto {

    private User user;
    private String message;
}
