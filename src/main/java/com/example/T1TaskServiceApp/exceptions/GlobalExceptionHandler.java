package com.example.T1TaskServiceApp.exceptions;

import com.example.T1TaskServiceApp.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(TaskNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponseDTO handleTaskNotFoundException(TaskNotFoundException ex) {
		return new ErrorResponseDTO(
				LocalDateTime.now(),
				HttpStatus.NOT_FOUND.value(),
				"Task Not Found",
				ex.getMessage()
		);
	}

	@ExceptionHandler(TaskCancellationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponseDTO handleTaskCancellationException(TaskCancellationException ex) {
		return new ErrorResponseDTO(
				LocalDateTime.now(),
				HttpStatus.BAD_REQUEST.value(),
				"Task Cancellation Error",
				ex.getMessage()
		);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponseDTO handleGenericException(Exception ex) {
		return new ErrorResponseDTO(
				LocalDateTime.now(),
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error",
				ex.getMessage()
		);
	}
}