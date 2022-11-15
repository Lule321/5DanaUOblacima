package com.five_days_in_cloud.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication(scanBasePackages= {"entities", "repository", "com.five_days_in_cloud.project", "entities.utilities"})
@EnableJpaRepositories("repository")
@EntityScan("entities")
@RestController
public class ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}
	
	@GetMapping("/")
	public String index()
	{
		return "Greetings from Spring Boot Project Application!";
	}
	
	

}
