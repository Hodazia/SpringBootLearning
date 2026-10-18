package com.example.firstspringprjct.DTO;

import com.example.firstspringprjct.Entity.TaskEntity;
import com.example.firstspringprjct.Entity.TaskStatus;

import java.util.List;

public class TaskResponse {
    private int id;
    private String description;
    private TaskStatus status;

    public TaskResponse(int id, String description, TaskStatus status) {
        this.id = id;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }
}
