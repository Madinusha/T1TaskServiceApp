package com.example.T1TaskServiceApp.service;

import static org.junit.jupiter.api.Assertions.*;
import com.example.T1TaskServiceApp.dto.TaskRequestDTO;
import com.example.T1TaskServiceApp.dto.TaskResponseDTO;
import com.example.T1TaskServiceApp.exceptions.TaskCancellationException;
import com.example.T1TaskServiceApp.model.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TaskServiceIntegrationTest {

	@Autowired
	private TaskService taskService;

	@Test
	@DisplayName("Жизненный цикл Task")
	void testFullTaskLifecycle() throws InterruptedException {
		TaskRequestDTO request = new TaskRequestDTO("Test task", 2L);
		TaskResponseDTO createdTask = taskService.createTask(request);

		assertNotNull(createdTask.getId());
		assertEquals("Test task", createdTask.getDescription());
		assertEquals(2L, createdTask.getDurationSeconds());
		assertEquals(TaskStatus.IN_PROGRESS.name(), createdTask.getStatus());
		assertNotNull(createdTask.getCreatedDate());
		assertNotNull(createdTask.getModifiedDate());

		List<TaskResponseDTO> allTasks = taskService.getAllTasks();
		assertEquals(1, allTasks.size());
		assertEquals(createdTask.getId(), allTasks.get(0).getId());

		Optional<TaskResponseDTO> foundTask = taskService.getTaskById(createdTask.getId());
		assertTrue(foundTask.isPresent());
		assertEquals(createdTask.getId(), foundTask.get().getId());

		TimeUnit.SECONDS.sleep(6);

		Optional<TaskResponseDTO> doneTask = taskService.getTaskById(createdTask.getId());
		assertTrue(doneTask.isPresent());
		assertEquals(TaskStatus.DONE.name(), doneTask.get().getStatus());

		TaskCancellationException exception = assertThrows(
				TaskCancellationException.class,
				() -> taskService.cancelTask(createdTask.getId())
		);
		assertEquals("Cannot cancel task: already DONE", exception.getMessage());
	}

	@Test
	@DisplayName("Отмена Task")
	void testTaskCancellation() {
		TaskRequestDTO request = new TaskRequestDTO("Cancellable task", 60L);
		TaskResponseDTO task = taskService.createTask(request);

		assertDoesNotThrow(() -> taskService.cancelTask(task.getId()));

		Optional<TaskResponseDTO> cancelledTask = taskService.getTaskById(task.getId());
		assertTrue(cancelledTask.isPresent());
		assertEquals(TaskStatus.CANCELED.name(), cancelledTask.get().getStatus());
	}

	@Test
	@DisplayName("Поиск несуществующей Task")
	void testTaskNotFound() {
		Optional<TaskResponseDTO> task = taskService.getTaskById(999L);
		assertFalse(task.isPresent());
	}
}