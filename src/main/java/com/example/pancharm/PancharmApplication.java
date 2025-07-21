package com.example.pancharm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PancharmApplication {

	public static void main(String[] args) {
		SpringApplication.run(PancharmApplication.class, args);
	}

}
