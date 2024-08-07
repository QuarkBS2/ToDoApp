package com.quarkbs.ToDoListApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class ToDoListAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToDoListAppApplication.class, args);
	}

}
