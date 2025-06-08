package com.example.T1TaskServiceApp.service;

import com.example.T1TaskServiceApp.dto.TaskRequestDTO;
import com.example.T1TaskServiceApp.dto.TaskResponseDTO;
import java.util.List;
import java.util.Optional;

public interface TaskService {
	List<TaskResponseDTO> getAllTasks();
	Optional<TaskResponseDTO> getTaskById(Long id);
	TaskResponseDTO createTask(TaskRequestDTO request);
	void cancelTask(Long id);
	void updateTaskStatuses();
}