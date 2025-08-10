package com.example.itemservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
		exclude = {
				org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
				org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
				org.springframework.boot.autoconfigure.jdbc.JdbcClientAutoConfiguration.class
		}
)
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

}
