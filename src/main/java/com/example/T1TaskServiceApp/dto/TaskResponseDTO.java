package com.example.T1TaskServiceApp.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskResponseDTO {
	private Long id;
	private String description;
	private Long durationSeconds;
	private String status;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;
}