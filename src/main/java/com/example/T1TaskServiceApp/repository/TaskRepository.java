package com.example.T1TaskServiceApp.repository;

import com.example.T1TaskServiceApp.model.Task;
import com.example.T1TaskServiceApp.model.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {
	List<Task> findAll();
	Optional<Task> findById(Long id);
	Task save(Task task);
	boolean cancelById(Long id);
	boolean updateStatusIfInProgress(Long id, TaskStatus newStatus, LocalDateTime updateTime);
}