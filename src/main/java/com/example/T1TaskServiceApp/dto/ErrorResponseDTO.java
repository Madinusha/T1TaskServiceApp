package com.example.T1TaskServiceApp.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponseDTO {
	private LocalDateTime timestamp;
	private int status;
	private String error;
	private String message;

	public ErrorResponseDTO(LocalDateTime timestamp, int status, String error, String message) {
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.message = message;
	}
}