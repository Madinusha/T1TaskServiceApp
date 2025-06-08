package com.example.T1TaskServiceApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class T1TaskServiceApp {
	public static void main(String[] args) {
		SpringApplication.run(T1TaskServiceApp.class, args);
	}
}