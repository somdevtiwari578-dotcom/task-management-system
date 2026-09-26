package com.taskmanagement.service;

import com.taskmanagement.dto.DashboardResponse;
import com.taskmanagement.dto.TaskRequest;
import com.taskmanagement.dto.TaskResponse;
import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.TaskPriority;
import com.taskmanagement.entity.TaskStatus;
import com.taskmanagement.entity.User;
import com.taskmanagement.exception.ResourceNotFoundException;
import com.taskmanagement.repository.TaskRepository;
import com.taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<TaskResponse> getMyTasks(String email) {
        User user = getUser(email);
        return taskRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(TaskResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse getMyTask(Long taskId, String email) {
        User user = getUser(email);
        Task task = taskRepository.findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        return TaskResponse.from(task);
    }

    @Transactional
    public TaskResponse createTask(TaskRequest request, String email) {
        User user = getUser(email);

        Task task = Task.builder()
                .title(request.title().trim())
                .description(normalize(request.description()))
                .status(request.status() == null ? TaskStatus.PENDING : request.status())
                .priority(request.priority() == null ? TaskPriority.MEDIUM : request.priority())
                .dueDate(request.dueDate())
                .user(user)
                .build();

        return TaskResponse.from(taskRepository.save(task));
    }

    @Transactional
    public TaskResponse updateTask(Long taskId, TaskRequest request, String email) {
        User user = getUser(email);
        Task task = taskRepository.findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        task.setTitle(request.title().trim());
        task.setDescription(normalize(request.description()));

        if (request.status() != null) {
            task.setStatus(request.status());
        }
        if (request.priority() != null) {
            task.setPriority(request.priority());
        }

        task.setDueDate(request.dueDate());

        return TaskResponse.from(taskRepository.save(task));
    }

    @Transactional
    public void deleteTask(Long taskId, String email) {
        User user = getUser(email);
        Task task = taskRepository.findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        taskRepository.delete(task);
    }

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard(String email) {
        User user = getUser(email);
        Long userId = user.getId();

        return new DashboardResponse(
                taskRepository.countByUserId(userId),
                taskRepository.countByUserIdAndStatus(userId, TaskStatus.PENDING),
                taskRepository.countByUserIdAndStatus(userId, TaskStatus.IN_PROGRESS),
                taskRepository.countByUserIdAndStatus(userId, TaskStatus.COMPLETED)
        );
    }

    private User getUser(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }
}
