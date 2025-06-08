package com.example.T1TaskServiceApp.service;

import com.example.T1TaskServiceApp.dto.TaskRequestDTO;
import com.example.T1TaskServiceApp.dto.TaskResponseDTO;
import com.example.T1TaskServiceApp.exceptions.TaskCancellationException;
import com.example.T1TaskServiceApp.mapper.TaskMapper;
import com.example.T1TaskServiceApp.model.Task;
import com.example.T1TaskServiceApp.model.TaskStatus;
import com.example.T1TaskServiceApp.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
	private final TaskRepository repository;
	private final TaskMapper taskMapper;

	@Override
	public List<TaskResponseDTO> getAllTasks() {
		return repository.findAll().stream()
				.map(taskMapper::toDto)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<TaskResponseDTO> getTaskById(Long id) {
		return repository.findById(id)
				.map(taskMapper::toDto);
	}

	private final AtomicLong idCounter = new AtomicLong(1);

	@Override
	public TaskResponseDTO createTask(TaskRequestDTO request) {
		Task task = new Task(
				idCounter.getAndIncrement(),
				request.getDescription(),
				request.getDurationSeconds(),
				TaskStatus.IN_PROGRESS,
				LocalDateTime.now(),
				LocalDateTime.now()
		);
		Task savedTask = repository.save(task);
		return taskMapper.toDto(savedTask);
	}

	@Override
	public void cancelTask(Long id) {
		Task task = repository.findById(id)
				.orElseThrow(() -> new TaskCancellationException("Task not found with id: " + id));

		if (task.getStatus() == TaskStatus.DONE) {
			throw new TaskCancellationException("Cannot cancel task: already DONE");
		}

		repository.cancelById(id);
	}

	@Scheduled(fixedRateString = "${task.status-check.interval}")
	public void updateTaskStatuses() {
		LocalDateTime now = LocalDateTime.now();
		repository.findAll().forEach(task -> {
			if (task.getStatus() == TaskStatus.IN_PROGRESS &&
					task.getCreatedDate().plusSeconds(task.getDurationSeconds()).isBefore(now)) {
				repository.updateStatusIfInProgress(task.getId(), TaskStatus.DONE, now);
			}
		});
	}

}


