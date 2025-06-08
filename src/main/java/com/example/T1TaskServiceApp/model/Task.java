package com.example.T1TaskServiceApp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
	private Long id;
	private String description;
	private long durationSeconds;
	private TaskStatus status = TaskStatus.IN_PROGRESS;
	private LocalDateTime createdDate = LocalDateTime.now();
	private LocalDateTime modifiedDate = LocalDateTime.now();
}
