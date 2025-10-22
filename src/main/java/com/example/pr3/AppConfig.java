package com.example.pr3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer{

	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration cfg = new CorsConfiguration();
		cfg.addAllowedOriginPattern("*");
		cfg.addAllowedHeader("*");
		cfg.addAllowedMethod("*");
		cfg.setAllowCredentials(false);
		UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
		src.registerCorsConfiguration("/**", cfg);
		
		return new CorsFilter(src);
	}
}
