package com.example.messagingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);

		new Thread(() -> new ClientMessagingService(1).setUpAndSendMessageToServer()).start();
		new Thread(() -> new ClientMessagingService(2).setUpAndSendMessageToServer()).start();
	}
}
