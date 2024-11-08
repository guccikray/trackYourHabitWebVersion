package com.guccikray.trackYourHabits.userEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private UUID userId;
    @Setter
    @NotBlank(message = "Name can't be blank")
    @Size(min = 3, max = 25, message = "Name should have length from 3 to 25")
    private String name;
    @Setter
    @Email(message = "Invalid email")
    @NotBlank(message = "Email can't be blank")
    @Size(min = 8, max = 30, message = "Email should have length from 8 to 30")
    private String email;
    @Setter
    @NotBlank(message = "Password can't be blank")
    private String password;


    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

}
