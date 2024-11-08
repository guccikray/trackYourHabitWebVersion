package com.guccikray.trackYourHabits.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignInDto {

    @NotBlank(message = "Email can't be blank")
    @Size(min = 8, max = 30, message = "Email should have length from 8 to 30")
    private String email;
    @NotBlank(message = "Password can't be blank")
    @Size(min = 5, max = 30, message = "Password should have length from 5 to 30")
    private String password;
}
