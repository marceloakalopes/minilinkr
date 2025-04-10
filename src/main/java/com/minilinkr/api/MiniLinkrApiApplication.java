package com.minilinkr.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MiniLinkrApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniLinkrApiApplication.class, args);
	}

//	@Bean
//	public WebMvcConfigurer webMvcConfigurer() {
//		return new WebMvcConfigurer() {
//		};
//	}

}
