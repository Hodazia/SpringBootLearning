package com.example.firstspringprjct.Entity;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class TaskEntity {

    private int id;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate dueDate;
    private LocalDateTime createdAt;

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskPriority getPriority() { return priority; }

    public void setPriority(TaskPriority priority)
    {
        this.priority = priority;
    }

    public LocalDate getDueDate()
    {
        return this.dueDate;
    }

    public void setDueDate(LocalDate date)
    {
        this.dueDate = date;
    }

    public LocalDateTime getCreatedAt()
    {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}


