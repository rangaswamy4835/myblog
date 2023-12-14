package com.myblog7;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication //It is starting point from where your project execution begins.
public class Myblog7Application {

	public static void main(String[] args) {
		SpringApplication.run(Myblog7Application.class, args);
	}
	@Bean // wHEN DO we use bean when an external library is added for which IOC is unable to create an object then we use @Bean annotation
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
