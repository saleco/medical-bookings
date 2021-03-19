package com.github.saleco.medicalbookings;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "Doctor API", version = "1.0", description = "Doctor API v1.0"))
@SpringBootApplication
public class MedicalBookingsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicalBookingsApplication.class, args);
    }

}
