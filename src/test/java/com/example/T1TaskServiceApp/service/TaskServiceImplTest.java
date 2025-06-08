package com.example.T1TaskServiceApp.service;

import com.example.T1TaskServiceApp.dto.TaskRequestDTO;
import com.example.T1TaskServiceApp.dto.TaskResponseDTO;
import com.example.T1TaskServiceApp.exceptions.TaskCancellationException;
import com.example.T1TaskServiceApp.mapper.TaskMapper;
import com.example.T1TaskServiceApp.model.Task;
import com.example.T1TaskServiceApp.model.TaskStatus;
import com.example.T1TaskServiceApp.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

	@Mock
	private TaskRepository repository;

	@Mock
	private TaskMapper taskMapper;

	@InjectMocks
	private TaskServiceImpl taskService;

	@Test
	void getAllTasks_ShouldReturnAllTasks() {
		Task task = new Task(1L, "Test", 10L, TaskStatus.IN_PROGRESS,
				LocalDateTime.now(), LocalDateTime.now());
		TaskResponseDTO dto = new TaskResponseDTO();
		when(repository.findAll()).thenReturn(List.of(task));
		when(taskMapper.toDto(task)).thenReturn(dto);

		List<TaskResponseDTO> result = taskService.getAllTasks();

		assertEquals(1, result.size());
		assertEquals(dto, result.get(0));
		verify(repository).findAll();
		verify(taskMapper).toDto(task);
	}

	@Test
	void getTaskById_WhenExists_ShouldReturnTask() {
		Long taskId = 1L;
		Task task = new Task(taskId, "Test", 10L, TaskStatus.IN_PROGRESS,
				LocalDateTime.now(), LocalDateTime.now());
		TaskResponseDTO dto = new TaskResponseDTO();
		when(repository.findById(taskId)).thenReturn(Optional.of(task));
		when(taskMapper.toDto(task)).thenReturn(dto);

		Optional<TaskResponseDTO> result = taskService.getTaskById(taskId);

		assertTrue(result.isPresent());
		assertEquals(dto, result.get());
		verify(repository).findById(taskId);
		verify(taskMapper).toDto(task);
	}

	@Test
	void createTask_ShouldCreateAndReturnTask() {
		TaskRequestDTO request = new TaskRequestDTO("New task", 5L);
		Task newTask = new Task(1L, request.getDescription(), request.getDurationSeconds(),
				TaskStatus.IN_PROGRESS, LocalDateTime.now(), LocalDateTime.now());
		Task savedTask = new Task(1L, "New task", 5L, TaskStatus.IN_PROGRESS,
				LocalDateTime.now(), LocalDateTime.now());
		TaskResponseDTO response = new TaskResponseDTO();

		when(repository.save(any(Task.class))).thenReturn(savedTask);
		when(taskMapper.toDto(savedTask)).thenReturn(response);

		TaskResponseDTO result = taskService.createTask(request);

		assertNotNull(result);
		assertEquals(response, result);
		verify(repository).save(any(Task.class));
		verify(taskMapper).toDto(savedTask);
	}

	@Test
	void cancelTask_WhenTaskExistsAndNotDone_ShouldCancel() {
		Long taskId = 1L;
		Task task = new Task(taskId, "Test", 10L, TaskStatus.IN_PROGRESS,
				LocalDateTime.now(), LocalDateTime.now());
		when(repository.findById(taskId)).thenReturn(Optional.of(task));
		when(repository.cancelById(taskId)).thenReturn(true);

		assertDoesNotThrow(() -> taskService.cancelTask(taskId));
		verify(repository).findById(taskId);
		verify(repository).cancelById(taskId);
	}

	@Test
	void cancelTask_WhenTaskNotFound_ShouldThrowException() {
		Long taskId = 1L;
		when(repository.findById(taskId)).thenReturn(Optional.empty());

		assertThrows(TaskCancellationException.class, () -> taskService.cancelTask(taskId));
		verify(repository).findById(taskId);
		verify(repository, never()).cancelById(any());
	}

	@Test
	void cancelTask_WhenTaskAlreadyDone_ShouldThrowException() {
		Long taskId = 1L;
		Task task = new Task(taskId, "Test", 10L, TaskStatus.DONE,
				LocalDateTime.now(), LocalDateTime.now());
		when(repository.findById(taskId)).thenReturn(Optional.of(task));

		TaskCancellationException exception = assertThrows(
				TaskCancellationException.class,
				() -> taskService.cancelTask(taskId)
		);
		assertEquals("Cannot cancel task: already DONE", exception.getMessage());
		verify(repository).findById(taskId);
		verify(repository, never()).cancelById(any());
	}

	@Test
	void updateTaskStatuses_ShouldUpdateExpiredTasks() {
		LocalDateTime testTime = LocalDateTime.of(2023, 1, 1, 12, 0);
		LocalDateTime expiredTime = testTime.minusSeconds(10);

		Task expiredTask = new Task(1L, "Expired", 5L, TaskStatus.IN_PROGRESS,
				expiredTime, expiredTime);
		Task activeTask = new Task(2L, "Active", 15L, TaskStatus.IN_PROGRESS,
				testTime.minusSeconds(5), testTime.minusSeconds(5));

		when(repository.findAll()).thenReturn(List.of(expiredTask, activeTask));

		when(repository.updateStatusIfInProgress(eq(1L), eq(TaskStatus.DONE), any(LocalDateTime.class)))
				.thenReturn(true);

		taskService.updateTaskStatuses();

		verify(repository).findAll();
		verify(repository).updateStatusIfInProgress(eq(1L), eq(TaskStatus.DONE), any(LocalDateTime.class));
	}
}