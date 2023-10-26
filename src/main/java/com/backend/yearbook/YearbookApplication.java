package com.backend.yearbook;

import com.backend.yearbook.authentication.AuthService;
import com.backend.yearbook.authentication.AuthenticationRequest;
import com.backend.yearbook.authentication.RegisterRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.backend.yearbook.entity.Role.ADMIN;

@SpringBootApplication
public class YearbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(YearbookApplication.class, args);
	}
    @Bean
    public CommandLineRunner commandLineRunner(AuthService service){
        return args -> {
            var admin= AuthenticationRequest.builder()

                .email("admin@gmail.com")
                .password("password").build();

            System.out.println(service.authenticate(admin).getToken());
        };
    }

}
