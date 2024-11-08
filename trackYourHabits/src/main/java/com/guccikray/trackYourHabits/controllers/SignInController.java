package com.guccikray.trackYourHabits.controllers;

import com.guccikray.trackYourHabits.dto.SignInDto;
import com.guccikray.trackYourHabits.exceptions.EmailAlreadyExistsException;
import com.guccikray.trackYourHabits.exceptions.WrongInputDataException;
import com.guccikray.trackYourHabits.dto.JwtResponseDto;
import com.guccikray.trackYourHabits.jwt.JwtUtil;
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
public class SignInController {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public SignInController(CustomUserDetailsService customUserDetailsService, JwtUtil jwtUtil) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInDto signInRequest) {

        try {
            User signedInUser = customUserDetailsService.sighIn(signInRequest.getEmail(), signInRequest.getPassword());
            String token = jwtUtil.generateToken(signedInUser.getUserId().toString());
            return ResponseEntity.ok(new JwtResponseDto(token));
        } catch (EmailAlreadyExistsException | WrongInputDataException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }
}
