package com.ksb.micro.profile_photo;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.ksb.micro.profile_photo")
public class ProfilePhotoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProfilePhotoApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Profile photo Service API")
						.description("Profile photo Service")
						.version("1.0.0"));
	}
}
