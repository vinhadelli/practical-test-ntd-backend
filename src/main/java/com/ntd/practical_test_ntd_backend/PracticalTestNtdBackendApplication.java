package com.ntd.practical_test_ntd_backend;

import com.ntd.practical_test_ntd_backend.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PracticalTestNtdBackendApplication {

	@Autowired
	UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(PracticalTestNtdBackendApplication.class, args);
	}
}
