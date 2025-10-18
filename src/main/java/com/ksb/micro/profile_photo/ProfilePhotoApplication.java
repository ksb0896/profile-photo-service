package com.ksb.micro.profile_photo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/*@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.ksb.micro.profile_photo")*/
@SpringBootApplication(scanBasePackages = "com.ksb.micro.profile_photo")
public class ProfilePhotoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProfilePhotoApplication.class, args);
	}

}
