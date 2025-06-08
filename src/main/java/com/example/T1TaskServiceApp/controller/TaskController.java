package com.example.T1TaskServiceApp.controller;

import com.example.T1TaskServiceApp.dto.TaskRequestDTO;
import com.example.T1TaskServiceApp.dto.TaskResponseDTO;
import com.example.T1TaskServiceApp.exceptions.TaskNotFoundException;
import com.example.T1TaskServiceApp.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {
	private final TaskService taskService;

	@GetMapping
	public List<TaskResponseDTO> getAllTasks() {
		return taskService.getAllTasks();
	}

	@GetMapping("/{id}")
	public TaskResponseDTO getTaskById(@PathVariable Long id) {
		return taskService.getTaskById(id)
				.orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
	}

	@PostMapping
	public TaskResponseDTO createTask(@Valid @RequestBody TaskRequestDTO request) {
		return taskService.createTask(request);
	}

	@DeleteMapping("/{id}")
	public void cancelTask(@PathVariable Long id) {
		taskService.cancelTask(id);
	}
}