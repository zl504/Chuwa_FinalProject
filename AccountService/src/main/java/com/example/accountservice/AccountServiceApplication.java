package com.example.accountservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@SpringBootApplication(scanBasePackages = "com.example.accountservice")
public class AccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountServiceApplication.class, args);
	}
//	@Bean
//	public CommandLineRunner showEndpoints(ApplicationContext ctx) {
//		return args -> {
//			RequestMappingHandlerMapping mapping = ctx.getBean(RequestMappingHandlerMapping.class);
//			mapping.getHandlerMethods().forEach((key, value) -> {
//				System.out.println(key + " => " + value);
//			});
//		};
//	}
}
