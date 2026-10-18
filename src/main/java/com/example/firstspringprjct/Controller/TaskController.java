package com.example.firstspringprjct.Controller;

import com.example.firstspringprjct.DTO.TaskRequest;
import com.example.firstspringprjct.DTO.TaskResponse;
import com.example.firstspringprjct.Entity.PageResponse;
import com.example.firstspringprjct.Entity.TaskEntity;
import com.example.firstspringprjct.Entity.TaskPriority;
import com.example.firstspringprjct.Entity.TaskStatus;
import com.example.firstspringprjct.Services.FileValidationService;
import com.example.firstspringprjct.Services.TaskService;
import com.example.firstspringprjct.Utils.FileMetadata;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    private TaskService taskservice;

    @Autowired
    private FileValidationService fileService;

    @Autowired
    public TaskController(TaskService taskservice)
    {
        this.taskservice = taskservice;
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthcheck()
    {
        return ResponseEntity.ok("endpoint is healthy");
    }

    @PostMapping()
    public ResponseEntity<TaskResponse> addTask(@RequestBody TaskRequest request)
    {
        TaskResponse response = taskservice.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping()
    public ResponseEntity<List<TaskResponse>> getTasks()
    {
        List<TaskResponse> tasks = taskservice.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTasksbyId(@PathVariable int id)
    {
        return ResponseEntity.ok(taskservice.getTaskById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable int id,
            @Valid @RequestBody TaskRequest request) {

        return ResponseEntity.ok(
                taskservice.updateTask(id, request)
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(
            @PathVariable TaskStatus status) {

        return ResponseEntity.ok(
                taskservice.getTasksByStatus(status)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable int id) {

        taskservice.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<TaskResponse>> searchTasks(
            @RequestParam(required=false) TaskStatus status,
            @RequestParam(required=false) TaskPriority priority,
            @RequestParam(required=false) String keyword,
            @RequestParam(required=false)
            @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
            LocalDate dueBefore,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dueAfter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction)
    {
        return ResponseEntity.ok(taskservice.searchTasks(
                status, priority, keyword, dueBefore, dueAfter,
                page, size, sortBy, direction));
    }

    @DeleteMapping("/completed")
    public ResponseEntity<Map<String,Integer>> deleteCompletedTasks()
    {
        int deletedCount = taskservice.deleteCompletedTasks();

        return ResponseEntity.ok(
                Map.of("deletedCount", deletedCount)
        );
    }


//    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<FileMetadataResponse> uploadFile(
//            @RequestParam("file" MultipartFile file))
//    {
//        FileMetadataResponse = fileService.up
//    }


}
