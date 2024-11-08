package com.guccikray.trackYourHabits.repositories;

import com.guccikray.trackYourHabits.habitEntity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HabitRepository extends JpaRepository<Habit, String> {

    boolean existsByNameAndUserId(String name, UUID userId);
    List<Habit> findByUserId(UUID userId);
    Optional<Habit> findByNameAndUserId(String habitName, UUID userId);
}
