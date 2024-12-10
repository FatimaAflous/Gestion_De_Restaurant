package com.projet_restaurant.serviceutilisateurs;

import com.projet_restaurant.serviceutilisateurs.Configuration.RSAConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.LinkedHashMap;
import java.util.Map;

@SpringBootApplication
@EnableConfigurationProperties(RSAConfig.class)
public class ServiceUtilisateursApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceUtilisateursApplication.class, args);
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		// Définir une map pour les différents encoders
		Map<String, PasswordEncoder> encoders = new LinkedHashMap<>();
		encoders.put("bcrypt", new BCryptPasswordEncoder());
		return new DelegatingPasswordEncoder("bcrypt", encoders);
	}
}
