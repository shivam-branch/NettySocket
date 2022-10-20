package com.socket.socketPractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SocketPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocketPracticeApplication.class, args);
	}

}
