package com.guccikray.trackYourHabits.repositories;

import com.guccikray.trackYourHabits.habitEntity.HabitProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HabitProgressRepository extends JpaRepository<HabitProgress, String> {

    List<HabitProgress> getByNameAndUserId(String name, UUID userId);
}
