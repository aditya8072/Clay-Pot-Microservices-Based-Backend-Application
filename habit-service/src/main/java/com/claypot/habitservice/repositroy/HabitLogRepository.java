package com.claypot.habitservice.repositroy;

import com.claypot.habitservice.entity.Habit;
import com.claypot.habitservice.entity.HabitLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HabitLogRepository extends JpaRepository<HabitLog, Long> {
    Optional<HabitLog> findByHabitIdAndDate(Long habitId, LocalDate date);

    List<HabitLog> findByHabitId(Habit habit);

    List<HabitLog> findByHabitIdOrderByDateDesc(Long habitId);

    List<HabitLog> findByHabitIdAndDateBetween(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    );
}
