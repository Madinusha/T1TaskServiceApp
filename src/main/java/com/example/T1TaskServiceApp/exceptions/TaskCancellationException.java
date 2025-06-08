package com.example.T1TaskServiceApp.exceptions;

public class TaskCancellationException extends RuntimeException {
	public TaskCancellationException(String message) {
		super(message);
	}
}