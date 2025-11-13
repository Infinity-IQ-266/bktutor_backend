package com.bktutor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BkTutorApplication {

	public static void main(String[] args) {
		SpringApplication.run(BkTutorApplication.class, args);
	}

}
