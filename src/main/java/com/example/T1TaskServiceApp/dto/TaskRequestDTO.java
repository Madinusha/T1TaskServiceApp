package com.example.T1TaskServiceApp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequestDTO {
	@NotBlank(message = "Description is required")
	private String description;

	@Positive(message = "Duration must be > 0")
	private long durationSeconds;

	public TaskRequestDTO(String description, long durationSeconds) {
		this.description = description;
		this.durationSeconds = durationSeconds;
	}

}