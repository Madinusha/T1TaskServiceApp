package com.example.T1TaskServiceApp.repository;

import com.example.T1TaskServiceApp.model.Task;
import com.example.T1TaskServiceApp.model.TaskStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryTaskRepository implements TaskRepository {
	private final Map<Long, Task> tasks = new ConcurrentHashMap<>();

	public List<Task> findAll() {
		return new ArrayList<>(tasks.values());
	}

	public Optional<Task> findById(Long id) {
		return Optional.ofNullable(tasks.get(id));
	}

	public Task save(Task task) {
		task.setModifiedDate(LocalDateTime.now());
		tasks.put(task.getId(), task);
		return task;
	}

	public boolean cancelById(Long id) {
		return tasks.computeIfPresent(id, (key, task) -> {
			task.setStatus(TaskStatus.CANCELED);
			task.setModifiedDate(LocalDateTime.now());
			return task;
		}) != null;
	}

	@Override
	public boolean updateStatusIfInProgress(Long id, TaskStatus newStatus, LocalDateTime updateTime) {
		return tasks.computeIfPresent(id, (key, task) -> {
			if (task.getStatus() == TaskStatus.IN_PROGRESS) {
				task.setStatus(newStatus);
				task.setModifiedDate(updateTime);
			}
			return task;
		}) != null;
	}
}
