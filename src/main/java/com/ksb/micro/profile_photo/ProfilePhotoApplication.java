package com.ksb.micro.profile_photo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.ksb.micro.profile_photo")
public class ProfilePhotoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProfilePhotoApplication.class, args);
	}

}
