package deti.tqs.cinemax.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {

		registry.addMapping("/api/**")
			.allowedOrigins("http://deti-tqs-10.ua.pt:4200")
			.allowedMethods("PUT", "DELETE", "GET", "POST")
			.allowedHeaders("*")
			.exposedHeaders("*") 
			.allowCredentials(true).maxAge(3600);

		// Add more mappings...
	}
}

