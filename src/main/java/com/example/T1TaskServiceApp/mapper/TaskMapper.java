package com.example.T1TaskServiceApp.mapper;

import com.example.T1TaskServiceApp.dto.TaskResponseDTO;
import com.example.T1TaskServiceApp.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
	public TaskResponseDTO toDto(Task task) {
		TaskResponseDTO dto = new TaskResponseDTO();
		dto.setId(task.getId());
		dto.setDescription(task.getDescription());
		dto.setDurationSeconds(task.getDurationSeconds());
		dto.setStatus(task.getStatus().name());
		dto.setCreatedDate(task.getCreatedDate());
		dto.setModifiedDate(task.getModifiedDate());
		return dto;
	}
}
