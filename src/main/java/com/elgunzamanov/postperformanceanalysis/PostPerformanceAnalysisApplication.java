package com.elgunzamanov.postperformanceanalysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class PostPerformanceAnalysisApplication {
	public static void main(String[] args) {
		SpringApplication.run(PostPerformanceAnalysisApplication.class, args);
	}
}
