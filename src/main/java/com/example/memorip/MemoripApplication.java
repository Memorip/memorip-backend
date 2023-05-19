package com.example.memorip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class MemoripApplication {
	public static void main(String[] args) {
		SpringApplication.run(MemoripApplication.class, args);
	}

}
