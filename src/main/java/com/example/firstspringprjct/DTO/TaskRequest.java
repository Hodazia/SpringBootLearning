package com.example.firstspringprjct.DTO;


import com.example.firstspringprjct.Entity.TaskPriority;
import com.example.firstspringprjct.Entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class TaskRequest {
    //int id;  the server should assign the id, rather than client

    @NotBlank(message = "Description cannot be empty")
    @Size(max = 200, message = "Description must be at most 200 characters ")
    private String description;

    @NotNull(message = "Status is required")
    private TaskStatus status;

    @NotNull(message = "Priority is required")
    private TaskPriority priority;

    private LocalDate dueDate;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
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
}
