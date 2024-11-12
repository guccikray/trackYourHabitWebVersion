package com.guccikray.trackYourHabits.services;

import com.guccikray.trackYourHabits.exceptions.HabitAlreadyMarkedAsCompletedTodayException;
import com.guccikray.trackYourHabits.exceptions.HabitNotFoundException;
import com.guccikray.trackYourHabits.habitEntity.HabitProgress;
import com.guccikray.trackYourHabits.repositories.HabitProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class HabitProgressService {

    private final HabitService habitService;
    private final HabitProgressRepository habitProgressRepository;

    @Autowired
    public HabitProgressService(HabitService habitService, HabitProgressRepository habitProgressRepository) {
        this.habitService = habitService;
        this.habitProgressRepository = habitProgressRepository;
    }

    public HabitProgress addHabitProgress(HabitProgress habitProgress,String userId) throws HabitNotFoundException,
            HabitAlreadyMarkedAsCompletedTodayException {
        if (!habitService.isHabitExists(habitProgress.getName(), userId)) {
            throw new HabitNotFoundException("Habit with this name doesn't exists");
        }
        List<HabitProgress> allHabitProgress = habitProgressRepository.getByNameAndUserId(habitProgress.getName(),
                UUID.fromString(userId));

        boolean isHabitProgressWithThisDateExists = allHabitProgress.stream()
                        .anyMatch(existedHabitProgress -> existedHabitProgress.getDateOfCompletion()
                                .equals(LocalDate.now()));

        if (isHabitProgressWithThisDateExists) {
            throw new HabitAlreadyMarkedAsCompletedTodayException("Habit already mark as completed today");
        }

        habitProgress.setUserId(UUID.fromString(userId));
        habitProgress.setCompleted(habitProgress.isCompleted());

        return habitProgressRepository.save(habitProgress);
    }

    public List<HabitProgress> getAllHabitProgress(String name, String userId) throws HabitNotFoundException {
        if (!habitService.isHabitExists(name, userId)) {
            throw new HabitNotFoundException("Habit with this name doesn't exists");
        }

        return habitProgressRepository.getByNameAndUserId(name, UUID.fromString(userId));
    }

    public long countCompletedDays(String name, String userId) throws HabitNotFoundException {
        if (!habitService.isHabitExists(name, userId)) {
            throw new HabitNotFoundException("Habit with this name doesn't exists");
        }

        List<HabitProgress> habitProgresses = habitProgressRepository.getByNameAndUserId(name,
                UUID.fromString(userId));

        return habitProgresses.stream()
                .filter(HabitProgress::isCompleted)
                .count();
    }
}
