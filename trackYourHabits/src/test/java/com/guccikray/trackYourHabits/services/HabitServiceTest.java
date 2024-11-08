package com.guccikray.trackYourHabits.services;

import com.guccikray.trackYourHabits.exceptions.HabitAlreadyExistsException;
import com.guccikray.trackYourHabits.exceptions.HabitNotFoundException;
import com.guccikray.trackYourHabits.habitEntity.Frequency;
import com.guccikray.trackYourHabits.habitEntity.Habit;
import com.guccikray.trackYourHabits.repositories.HabitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HabitServiceTest {
    @Mock
    private HabitRepository habitRepository;

    @InjectMocks
    private HabitService habitService;

    @Test
    void createHabitTest_shouldThrowHabitAlreadyExistsException_whenHabitExists() {
        Habit newHabit = new Habit("exercise", "exercise every day", Frequency.DAILY);
        String userId = "d2f1c9e7-88c9-4c68-89f9-cf8c8d9a6b0c";
        when(habitRepository.existsByNameAndUserId(newHabit.getName(), UUID.fromString(userId))).thenReturn(true);

        assertThatThrownBy(() -> habitService.createHabit(newHabit, userId))
                .isInstanceOf(HabitAlreadyExistsException.class)
                .hasMessage("Habit with this name already exists");
    }

    @Test
    void createHabitTest_shouldReturnUser_whenSuccessfullyCreated() throws HabitAlreadyExistsException {
        Habit newHabit = new Habit("exercise", "exercise every day", Frequency.DAILY);
        String userId = "d2f1c9e7-88c9-4c68-89f9-cf8c8d9a6b0c";
        when(habitRepository.existsByNameAndUserId(newHabit.getName(), UUID.fromString(userId))).thenReturn(false);
        when(habitRepository.save(newHabit)).thenReturn(newHabit);

        Habit createdHabit = habitService.createHabit(newHabit, userId);

        assertThat(createdHabit.getName()).isEqualTo("exercise");
        assertThat(createdHabit.getUserId()).isEqualTo(UUID.fromString(userId));
        verify(habitRepository, times(1)).save(newHabit);
    }

    @Test
    void getAllHabitsTest_shouldReturnListOfHabits_whenUserHasHabits() {
        Habit exerciseHabit = new Habit("exercise", "exercise every day", Frequency.DAILY);
        Habit joggingHabit = new Habit("jogging", "run every week", Frequency.WEEKLY);
        String userId = "d2f1c9e7-88c9-4c68-89f9-cf8c8d9a6b0c";

        exerciseHabit.setUserId(UUID.fromString(userId));
        joggingHabit.setUserId(UUID.fromString(userId));

        when(habitRepository.findByUserId(UUID.fromString(userId))).thenReturn(Arrays.asList(exerciseHabit, joggingHabit));

        List<Habit> habits = habitService.getAllUserHabits(userId);

        assertThat(habits).hasSize(2);
        assertThat(habits.get(0).getName()).isEqualTo("exercise");
        assertThat(habits.get(1).getName()).isEqualTo("jogging");
        assertThat(habits.get(1).getUserId()).isEqualTo(UUID.fromString(userId));

        verify(habitRepository, times(1)).findByUserId(UUID.fromString(userId));
    }

    @Test
    void getAllHabitsTest_shouldReturnEmptyList_whenUserHasNoHabits() {
        String userId = "d2f1c9e7-88c9-4c68-89f9-cf8c8d9a6b0c";

        when(habitRepository.findByUserId(UUID.fromString(userId))).thenReturn(List.of());

        List<Habit> habits = habitService.getAllUserHabits(userId);

        assertThat(habits).isEmpty();

        verify(habitRepository, times(1)).findByUserId(UUID.fromString(userId));
    }

    @Test
    void editHabitTest_shouldThrowHabitNotFoundException_whenHabitDoesntExists() {
        String userId = "d2f1c9e7-88c9-4c68-89f9-cf8c8d9a6b0c";
        Habit exerciseHabit = new Habit("exercise", "exercise every day", Frequency.DAILY);

        when(habitRepository.findByNameAndUserId(exerciseHabit.getName(), UUID.fromString(userId)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> habitService.editHabit(exerciseHabit, userId))
                .isInstanceOf(HabitNotFoundException.class)
                .hasMessage("Habit with this name doesn't exists");

        verify(habitRepository, times(1))
                .findByNameAndUserId(exerciseHabit.getName(), UUID.fromString(userId));
    }

    @Test
    void editHabitTest_shouldEditedHabit_whenHabitEditedSuccessfully() throws HabitNotFoundException {
        String userId = "d2f1c9e7-88c9-4c68-89f9-cf8c8d9a6b0c";
        Habit exerciseHabit = new Habit("exercise", "exercise every day", Frequency.DAILY);
        exerciseHabit.setUserId(UUID.fromString(userId));
        Habit editedHabit = new Habit("exercise", "exercise every week", Frequency.WEEKLY);

        when(habitRepository.findByNameAndUserId(exerciseHabit.getName(), UUID.fromString(userId)))
                .thenReturn(Optional.of(exerciseHabit));
        when(habitRepository.save(exerciseHabit)).thenReturn(editedHabit);

        Habit result = habitService.editHabit(editedHabit, userId);

        assertThat(result).isEqualTo(editedHabit);
        assertThat(result.getFrequency()).isEqualTo(Frequency.WEEKLY);
        assertThat(result.getDescription()).isEqualTo("exercise every week");

        verify(habitRepository, times(1)).save(exerciseHabit);
    }

    @Test
    void removeHabitTest_shouldThrowHabitNotFoundException_whenHabitDoesntExists() {
        String habitName = "exercise";
        String userId = "d2f1c9e7-88c9-4c68-89f9-cf8c8d9a6b0c";

        when(habitRepository.findByNameAndUserId(habitName, UUID.fromString(userId))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> habitService.removeHabit(habitName, userId))
                .isInstanceOf(HabitNotFoundException.class)
                .hasMessage("Habit with this name doesn't exists");

        verify(habitRepository, times(1))
                .findByNameAndUserId(habitName, UUID.fromString(userId));
    }

    @Test
    void removeHabitTest_shouldReturnNothing_whenHabitDeletesSuccessfully() throws HabitNotFoundException {
        String userId = "d2f1c9e7-88c9-4c68-89f9-cf8c8d9a6b0c";
        Habit exerciseHabit = new Habit("exercise", "exercise every day", Frequency.DAILY);
        exerciseHabit.setUserId(UUID.fromString(userId));

        when(habitRepository.findByNameAndUserId(exerciseHabit.getName(), UUID.fromString(userId)))
                .thenReturn(Optional.of(exerciseHabit));

        habitService.removeHabit(exerciseHabit.getName(), userId);

        verify(habitRepository, times(1)).delete(exerciseHabit);
    }
}
