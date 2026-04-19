package com.claypot.habitservice.entity;

import com.claypot.habitservice.utility.HabitFrequency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "habit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private HabitFrequency frequency;
    private boolean completedToday;


}
