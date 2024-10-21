package com.groupn.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.groupn.demo.repositories")
@SpringBootApplication
public class SwkomApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwkomApplication.class, args);
	}

}
