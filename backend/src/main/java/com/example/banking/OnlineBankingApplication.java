package com.example.banking;

import com.example.banking.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class OnlineBankingApplication {
	public static void main(String[] args) {
		SpringApplication.run(OnlineBankingApplication.class, args);
	}
} 