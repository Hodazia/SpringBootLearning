package com.example.firstspringprjct.Services;

import com.example.firstspringprjct.DTO.TaskRequest;
import com.example.firstspringprjct.DTO.TaskResponse;
import com.example.firstspringprjct.Entity.PageResponse;
import com.example.firstspringprjct.Entity.TaskEntity;
import com.example.firstspringprjct.Entity.TaskPriority;
import com.example.firstspringprjct.Entity.TaskStatus;
import com.example.firstspringprjct.Repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TaskService {

    public TaskRepository repository;

    TaskService(TaskRepository repo)
    {
        this.repository = repo;
    }

    public TaskResponse createTask(TaskRequest request)
    {
        TaskEntity task = new TaskEntity();
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());

        TaskEntity savedTask = repository.save(task);
        return mapToResponse(savedTask);
    }

    public TaskResponse mapToResponse(TaskEntity task)
    {
        return new TaskResponse(task.getId(), task.getDescription(), task.getStatus());
    }

    public TaskResponse getTaskById(int id) {
        TaskEntity task = findTaskOrThrow(id);
        return mapToResponse(task);
    }

    private TaskEntity findTaskOrThrow(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Task not found with ID: " + id
                ));
    }

    public List<TaskResponse> getAllTasks() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public TaskResponse updateTask(int id, TaskRequest request) {
        TaskEntity task = findTaskOrThrow(id);

        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());

        return mapToResponse(repository.save(task));
    }

    public TaskResponse updateStatus(int id, TaskStatus status) {
        TaskEntity task = findTaskOrThrow(id);

        task.setStatus(status);

        return mapToResponse(repository.save(task));
    }

    public List<TaskResponse> getTasksByStatus(TaskStatus status) {
        return repository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void deleteTask(int id) {
        boolean deleted = repository.deleteById(id);

        if (!deleted) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Task not found with ID: " + id
            );
        }
    }

    public PageResponse<TaskResponse> searchTasks(TaskStatus status, TaskPriority priority,
                                                  String keyword, LocalDate dueBefore,
                                                  LocalDate dueAfter,int page, int size,
                                                  String sortBy, String direction)
    {
        if (page < 0) {
            throw new IllegalArgumentException("Page cannot be negative");
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100");
        }

        Set<String> allowedSortFields =
                Set.of("id", "description", "priority", "status",
                        "dueDate", "createdAt");

        if (!allowedSortFields.contains(sortBy)) {
            throw new IllegalArgumentException("Unsupported sort field");
        }

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {
            throw new IllegalArgumentException(
                    "Direction must be asc or desc");
        }

        Stream<TaskEntity> stream = repository.findAll().stream();

        if (status != null) {
            stream = stream.filter(t -> t.getStatus() == status);
        }

        if (priority != null) {
            stream = stream.filter(t -> t.getPriority() == priority);
        }

        if (keyword != null && !keyword.isBlank()) {
            String search = keyword.trim().toLowerCase();

            stream = stream.filter(t ->
                    t.getDescription().toLowerCase().contains(search));
        }

        if (dueBefore != null) {
            stream = stream.filter(t ->
                    t.getDueDate() != null
                            && !t.getDueDate().isAfter(dueBefore));
        }

        if (dueAfter != null) {
            stream = stream.filter(t ->
                    t.getDueDate() != null
                            && !t.getDueDate().isBefore(dueAfter));
        }

        Comparator<TaskEntity> comparator = switch (sortBy) {
            case "id" -> Comparator.comparingInt(TaskEntity::getId);
            case "description" ->
                    Comparator.comparing(TaskEntity::getDescription,
                            String.CASE_INSENSITIVE_ORDER);
            case "priority" ->
                    Comparator.comparing(TaskEntity::getPriority);
            case "status" ->
                    Comparator.comparing(TaskEntity::getStatus);
            case "dueDate" ->
                    Comparator.comparing(TaskEntity::getDueDate,
                            Comparator.nullsLast(Comparator.naturalOrder()));
            case "createdAt" ->
                    Comparator.comparing(TaskEntity::getCreatedAt);
            default -> throw new IllegalArgumentException(
                    "Unsupported sort field");
        };

        if (direction.equalsIgnoreCase("desc")) {
            comparator = comparator.reversed();
        }

        List<TaskEntity> matching = stream
                .sorted(comparator)
                .toList();

        long total = matching.size();
        int from = (int) Math.min((long) page * size, total);
        int to = Math.min(from + size, matching.size());

        List<TaskResponse> content = matching.subList(from, to)
                .stream()
                .map(this::mapToResponse)
                .toList();

        int totalPages = (int) Math.ceil((double) total / size);

        return new PageResponse<>(
                content, page, size, total, totalPages,
                page + 1 < totalPages);


    }

    public int deleteCompletedTasks() {
        return repository.deleteCompleted();
    }

    public Map<TaskStatus, Long> countByStatus() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        TaskEntity::getStatus,
                        () -> new EnumMap<>(TaskStatus.class),
                        Collectors.counting()));
    }

    public Map<TaskPriority, Long> priorityCount()
    {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        TaskEntity::getPriority,
                        () -> new EnumMap<TaskPriority, Long>(TaskPriority.class),
                        Collectors.counting()
                ));
    }

    public int countAllTasks()
    {
        return repository.findAll().size();
    }

    public int CompletedTasks()
    {
        return repository.TasksCompleted();
    }
}
