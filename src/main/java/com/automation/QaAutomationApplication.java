package com.automation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;


@SpringBootApplication
@EnableHystrix
public class QaAutomationApplication {

	public static void main(String[] args) {
		SpringApplication.run(QaAutomationApplication.class, args);
	}

}
