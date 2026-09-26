package com.taskmanagement.controller;

import com.taskmanagement.dto.DashboardResponse;
import com.taskmanagement.dto.TaskRequest;
import com.taskmanagement.dto.TaskResponse;
import com.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public List<TaskResponse> getTasks(Authentication authentication) {
        return taskService.getMyTasks(authentication.getName());
    }

    @GetMapping("/{id}")
    public TaskResponse getTask(
            @PathVariable Long id,
            Authentication authentication) {
        return taskService.getMyTask(id, authentication.getName());
    }

    @PostMapping
    public TaskResponse createTask(
            @Valid @RequestBody TaskRequest request,
            Authentication authentication) {
        return taskService.createTask(request, authentication.getName());
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request,
            Authentication authentication) {
        return taskService.updateTask(id, request, authentication.getName());
    }

    @DeleteMapping("/{id}")
    public void deleteTask(
            @PathVariable Long id,
            Authentication authentication) {
        taskService.deleteTask(id, authentication.getName());
    }

    @GetMapping("/dashboard")
    public DashboardResponse dashboard(Authentication authentication) {
        return taskService.getDashboard(authentication.getName());
    }
}
