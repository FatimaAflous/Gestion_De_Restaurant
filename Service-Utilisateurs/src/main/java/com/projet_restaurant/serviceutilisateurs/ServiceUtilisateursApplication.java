package com.projet_restaurant.serviceutilisateurs;

import com.projet_restaurant.serviceutilisateurs.Configuration.RSAConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableConfigurationProperties(RSAConfig.class)
public class ServiceUtilisateursApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceUtilisateursApplication.class, args);
	}
	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
}
