package com.restclientdemo.send_message_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class PhoneBookServiceApplication {

	static void main(String[] args) {
		SpringApplication.run(PhoneBookServiceApplication.class, args);
	}

}
