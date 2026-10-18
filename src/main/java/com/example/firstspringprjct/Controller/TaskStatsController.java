package com.example.firstspringprjct.Controller;

import com.example.firstspringprjct.Entity.TaskPriority;
import com.example.firstspringprjct.Entity.TaskStatus;
import com.example.firstspringprjct.Services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class TaskStatsController {
    private final TaskService taskService;

    public TaskStatsController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks/by-status")
    public ResponseEntity<Map<TaskStatus, Long>> countByStatus() {
        return ResponseEntity.ok(taskService.countByStatus());
    }

    @GetMapping("/tasks/count")
    public ResponseEntity<Integer> totalTaskCount()
    {
        return ResponseEntity.ok(taskService.countAllTasks());
    }

    @GetMapping("/tasks/by-priority")
    public ResponseEntity<Map<TaskPriority, Long>> countByPriority()
    {
        return ResponseEntity.ok(taskService.priorityCount());
    }

    @GetMapping("/tasks/completed")
    public ResponseEntity<Integer> completedTasks()
    {
        return ResponseEntity.ok(taskService.CompletedTasks());
    }

}
