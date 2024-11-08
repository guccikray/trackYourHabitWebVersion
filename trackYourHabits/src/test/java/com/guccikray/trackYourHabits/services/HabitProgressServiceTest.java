package com.guccikray.trackYourHabits.services;

import com.guccikray.trackYourHabits.exceptions.HabitNotFoundException;
import com.guccikray.trackYourHabits.habitEntity.HabitProgress;
import com.guccikray.trackYourHabits.repositories.HabitProgressRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HabitProgressServiceTest {

    @Mock
    HabitProgressRepository habitProgressRepository;
    @Mock
    HabitService habitService;
    @InjectMocks
    HabitProgressService habitProgressService;

    @Test
    void addHabitProgressTest_throwsHabitNotFoundException_whenHabitDoesntExists() {
        String userId = "d2f1c9e7-88c9-4c68-89f9-cf8c8d9a6b0c";
        HabitProgress habitProgress = new HabitProgress("exercise", true);

        when(habitService.isHabitExists(habitProgress.getName(), UUID.fromString(userId))).thenReturn(false);

        assertThatThrownBy(() -> habitProgressService.addHabitProgress(habitProgress, userId))
                .isInstanceOf(HabitNotFoundException.class)
                .hasMessage("Habit with this name doesn't exists");

        verify(habitService, times(1))
                .isHabitExists(habitProgress.getName(), UUID.fromString(userId));
    }

    @Test
    void addHabitProgressTest_returnsHabitProgress_whenHabitExists() throws HabitNotFoundException {
        String userId = "d2f1c9e7-88c9-4c68-89f9-cf8c8d9a6b0c";
        HabitProgress habitProgress = new HabitProgress("exercise", true);

        when(habitService.isHabitExists(habitProgress.getName(), UUID.fromString(userId))).thenReturn(true);
        when(habitProgressRepository.save(habitProgress)).thenReturn(habitProgress);

        HabitProgress newHabitProgress = habitProgressService.addHabitProgress(habitProgress, userId);

        assertThat(newHabitProgress.getName()).isEqualTo(habitProgress.getName());
        assertThat(newHabitProgress.isCompleted()).isTrue();

        verify(habitService, times(1))
                .isHabitExists(habitProgress.getName(), UUID.fromString(userId));
        verify(habitProgressRepository, times(1)).save(habitProgress);
    }

    @Test
    void getAllHabitProgress_throwsHabitNotFoundException_whenHabitDoesntExists() {
        String name = "exercise";
        String userId = "d2f1c9e7-88c9-4c68-89f9-cf8c8d9a6b0c";

        when(habitService.isHabitExists(name, UUID.fromString(userId))).thenReturn(false);

        assertThatThrownBy(() -> habitProgressService.getAllHabitProgress(name, userId))
                .isInstanceOf(HabitNotFoundException.class)
                .hasMessage("Habit with this name doesn't exists");

        verify(habitService, times(1)).isHabitExists(name, UUID.fromString(userId));
    }

    @Test
    void getAllHabitProgress_returnsList_whenHabitExists () throws HabitNotFoundException {
        String name = "exercise";
        String userId = "d2f1c9e7-88c9-4c68-89f9-cf8c8d9a6b0c";

        HabitProgress habitProgressIsCompleted = new HabitProgress(name, true);
        HabitProgress habitProgressIsNotCompleted = new HabitProgress(name, false);
        List<HabitProgress> allHabitProgress = new ArrayList<>();
        allHabitProgress.add(habitProgressIsCompleted);
        allHabitProgress.add(habitProgressIsNotCompleted);

        when(habitService.isHabitExists(name, UUID.fromString(userId))).thenReturn(true);
        when(habitProgressRepository.getByNameAndUserId(name, UUID.fromString(userId))).thenReturn(allHabitProgress);

        List<HabitProgress> receivedAllHabitProgress = habitProgressService.getAllHabitProgress(name, userId);

        assertThat(receivedAllHabitProgress).hasSize(2);
        assertThat(receivedAllHabitProgress.get(0).getName()).isEqualTo("exercise");
        assertThat(receivedAllHabitProgress.get(1).getName()).isEqualTo("exercise");
        assertThat(receivedAllHabitProgress.get(0).isCompleted()).isTrue();
        assertThat(receivedAllHabitProgress.get(1).isCompleted()).isFalse();

        verify(habitService, times(1)).isHabitExists(name, UUID.fromString(userId));
        verify(habitProgressRepository, times(1))
                .getByNameAndUserId(name, UUID.fromString(userId));
    }

}
