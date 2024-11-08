package com.guccikray.trackYourHabits.services;

import com.guccikray.trackYourHabits.exceptions.HabitAlreadyExistsException;
import com.guccikray.trackYourHabits.exceptions.HabitNotFoundException;
import com.guccikray.trackYourHabits.habitEntity.Habit;
import com.guccikray.trackYourHabits.repositories.HabitRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class HabitService {

    private final HabitRepository habitRepository;

    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public boolean isHabitExists(String name, UUID userId) {
        return habitRepository.existsByNameAndUserId(name, userId);
    }

    public Habit createHabit(Habit habit, String userId) throws HabitAlreadyExistsException {
        if (isHabitExists(habit.getName(), UUID.fromString(userId))) {
            throw new HabitAlreadyExistsException("Habit with this name already exists");
        }
        habit.setUserId(UUID.fromString(userId));
        return habitRepository.save(habit);
    }

    public List<Habit> getAllUserHabits(String userId) {
        return habitRepository.findByUserId(UUID.fromString(userId));
    }

    public Habit editHabit(Habit editedHabit, String userId) throws HabitNotFoundException {
        Habit habit = habitRepository.findByNameAndUserId(editedHabit.getName(), UUID.fromString(userId))
                .orElseThrow(() -> new HabitNotFoundException("Habit with this name doesn't exists"));

        habit.setDescription(editedHabit.getDescription());
        habit.setFrequency(editedHabit.getFrequency());

        return habitRepository.save(habit);
    }

    public void removeHabit(String habitToRemove, String userId) throws HabitNotFoundException {
        Habit habit = habitRepository.findByNameAndUserId(habitToRemove, UUID.fromString(userId))
                .orElseThrow(() -> new HabitNotFoundException("Habit with this name doesn't exists"));

        habitRepository.delete(habit);
    }
}
