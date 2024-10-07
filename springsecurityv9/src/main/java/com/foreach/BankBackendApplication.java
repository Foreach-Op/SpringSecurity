package com.foreach;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
//@EnableJpaRepositories("com.foreach.repository")
//@EntityScan("com.foreach.model")
public class BankBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankBackendApplication.class, args);
	}

}
