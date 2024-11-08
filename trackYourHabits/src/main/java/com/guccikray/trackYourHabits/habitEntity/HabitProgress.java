package com.guccikray.trackYourHabits.habitEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Entity
@Table(name = "habit_progress")
@NoArgsConstructor
public class HabitProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Setter
    private String name;
    @CreationTimestamp
    private LocalDate dateOfCompletion;
    @Setter
    private boolean completed;
    @Setter
    private UUID userId;

    public HabitProgress (String name, boolean completed) {
        this.name = name;
        this.completed = completed;
    }

}
