package com.claypot.habitservice.controller;

import com.claypot.habitservice.dto.ApiResponse;
import com.claypot.habitservice.dto.HabitCheckInRequest;
import com.claypot.habitservice.entity.Habit;
import com.claypot.habitservice.entity.HabitLog;
import com.claypot.habitservice.service.HabitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/habits")
public class HabitController {
    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @PostMapping
    public ResponseEntity<Habit> createHabit(
            @RequestHeader("X-user-ID") Long userId,
            @RequestBody Map<String,String> body) {
        Habit habit = habitService.createHabit(userId, body.get("name"));
        return ResponseEntity.ok(habit);
    }

    @GetMapping
    public ResponseEntity<Iterable<Habit>> getHabits(
            @RequestHeader("X-user-ID") Long userId) {
        return ResponseEntity.ok(habitService.getHabits(userId));
    }

    @PostMapping("/{habitId}/checkin")
    public ResponseEntity<?> checkIn(
            @RequestHeader("X-User-ID") Long userId,
            @PathVariable Long habitId,
            @RequestBody Map<String, Boolean> body) {

        boolean completed = body.getOrDefault("completed", false);

        Optional<HabitLog> result = habitService.checkIn(userId, habitId, completed);

        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of(
                            "success", false,
                            "message", "Habit not found or does not belong to user"
                    )
            );
        }

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Check-in recorded",
                        "data", result.get()
                )
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteHabit(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id
    ) {

        ResponseEntity<ApiResponse> response;

        try {
            boolean deleted = habitService.deleteHabit(userId, id);

            if (!deleted) {
                response = ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Habit not found"));
            } else {
                response = ResponseEntity.ok(
                        new ApiResponse(true, "Habit deleted successfully")
                );
            }

        } catch (IllegalStateException e) {
            response = ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse(false, "You are not allowed to delete this habit"));
        }

        return response;
    }

}
