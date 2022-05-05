package com.example.springBootWithRealcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringBootWithRealcodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWithRealcodeApplication.class, args);
		System.out.print("testing purpose committed");
		List<String> list =  new ArrayList();
	}

}
