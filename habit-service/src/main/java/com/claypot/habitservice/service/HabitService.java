package com.claypot.habitservice.service;

import com.claypot.habitservice.entity.Habit;
import com.claypot.habitservice.entity.HabitLog;
import com.claypot.habitservice.repositroy.HabitLogRepository;
import com.claypot.habitservice.repositroy.HabitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class HabitService {
    private final HabitRepository habitRepository;
    private final HabitLogRepository habitLogRepository;

    public HabitService(HabitRepository habitRepository, HabitLogRepository habitLogRepository) {
        this.habitRepository = habitRepository;
        this.habitLogRepository = habitLogRepository;
    }

    public Habit createHabit(Long userId, String name) {
        Habit habit = new Habit();
        habit.setUserId(userId);
        habit.setName(name);
        habit.setCompletedToday(false);
        return habitRepository.save(habit);

    }

    public List<Habit> getHabits(Long userId) {
        return habitRepository.findByUserId(userId);
    }

    public Optional<HabitLog> checkIn(Long userId, Long habitId, boolean completed) {
        Optional<Habit> habit = habitRepository.findByIdAndUserId(habitId, userId);
        if (habit.isEmpty()) {
            return Optional.empty();
        }
        if (!habit.get().getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        LocalDate today = LocalDate.now();
        HabitLog habitLog = habitLogRepository
                .findByHabitIdAndDate(habitId, today)
                .orElseGet(() -> {
                    HabitLog newLog = new HabitLog();
                    newLog.setHabit(habit.get());
                    newLog.setDate(today);
                    return newLog;
                });
        habitLog.setCompleted(completed);
        habitLog.setCompletedAt(completed ? java.time.LocalDateTime.now() : null);

        return Optional.of(habitLogRepository.save(habitLog));
    }

    public boolean deleteHabit(Long userId, Long habitId) {

        Habit habit = habitRepository.findById(habitId).orElse(null);

        if (habit == null) {
            return false;
        }

        if (!habit.getUserId().equals(userId)) {
            throw new IllegalStateException("UNAUTHORIZED");
        }

        habitRepository.delete(habit);
        return true;
    }

}
