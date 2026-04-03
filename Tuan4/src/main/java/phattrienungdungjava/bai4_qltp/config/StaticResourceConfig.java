package phattrienungdungjava.bai4_qltp.config;

import java.nio.file.Path;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Path uploadDir = Path.of("static", "images").toAbsolutePath().normalize();
		registry.addResourceHandler("/images/**").addResourceLocations(uploadDir.toUri().toString());
	}
}
