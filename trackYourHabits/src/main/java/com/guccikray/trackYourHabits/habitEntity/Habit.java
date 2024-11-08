package com.guccikray.trackYourHabits.habitEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Entity
@Table(name = "habits")
@NoArgsConstructor
public class Habit {
    @Id
    @Setter
    @NotBlank(message = "Habit name can't be blank")
    @Size(min = 5, max = 30, message = "Name should have length from 3 to 30")
    private String name;
    @Setter
    private String description;
    @Setter
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Frequency can't be null")
    private Frequency frequency;
    @CreationTimestamp
    private LocalDate dateOfCreatingHabit;
    @Setter
    private UUID userId;

    public Habit(String name, String description, Frequency frequency) {
        this.name = name;
        this.description = description;
        this.frequency = frequency;
    }


}
