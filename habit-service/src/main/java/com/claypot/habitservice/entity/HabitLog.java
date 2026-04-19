package com.claypot.habitservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(
        name = "habit_logs",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"habit_id", "date"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HabitLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit;

    @Column(name = "checkin_date",nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private boolean completed;

    private LocalDateTime completedAt;
}
