package com.guccikray.trackYourHabits.controllers;

import com.guccikray.trackYourHabits.dto.RegistrationDto;
import com.guccikray.trackYourHabits.exceptions.EmailAlreadyExistsException;
import com.guccikray.trackYourHabits.services.CustomUserDetailsService;
import com.guccikray.trackYourHabits.userEntity.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public RegistrationController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @PostMapping("/registration")
    public ResponseEntity<RegistrationDto> registerUser(@Valid @RequestBody User user) {
        try {
            User registeredUser = customUserDetailsService.registerUser(user);
            return ResponseEntity.ok(new RegistrationDto(registeredUser, "User created successfully"));
        } catch (EmailAlreadyExistsException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new RegistrationDto(null, e.getMessage()));
        }
    }
}
