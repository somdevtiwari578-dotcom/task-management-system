package com.taskmanagement.dto;

public record DashboardResponse(
        long totalTasks,
        long pendingTasks,
        long inProgressTasks,
        long completedTasks
) {}
