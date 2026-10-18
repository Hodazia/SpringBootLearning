package com.example.firstspringprjct.Repository;

import com.example.firstspringprjct.Entity.TaskEntity;
import com.example.firstspringprjct.Entity.TaskStatus;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class TaskRepository {

    private final Map<Integer,TaskEntity> tasks = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);
    public TaskEntity save(TaskEntity task) {
        if (task.getId() == 0) {
            task.setId(idGenerator.getAndIncrement());
        }

        tasks.put(task.getId(), task);
        return task;
    }

    public List<TaskEntity> findAll() {
        return new ArrayList<>(tasks.values());
    }

    public Optional<TaskEntity> findById(int id) {
        return Optional.ofNullable(tasks.get(id));
    }

    public List<TaskEntity> findByStatus(TaskStatus status) {
        List<TaskEntity> result = new ArrayList<>();

        for (TaskEntity task : tasks.values()) {
            if (task.getStatus() == status) {
                result.add(task);
            }
        }

        return result;
    }

    public boolean deleteById(int id) {
        return tasks.remove(id) != null;
    }

    public int deleteCompleted() {
        int before = tasks.size();

        tasks.entrySet().removeIf(
                entry -> entry.getValue().getStatus()
                        == TaskStatus.COMPLETED);

        return before - tasks.size();
    }

    public int TasksCompleted()
    {
        List<TaskEntity> no = findAll().stream().
                filter(taskentity -> taskentity.getStatus()==TaskStatus.COMPLETED)
                .collect(Collectors.toList());

        return no.size();
    }

}
