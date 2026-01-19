package org.microservices.enrollment.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(new Server().url("/enrollment")
                        .description("Enrollment Service API Server")))
                .info(new Info()
                        .title("Enrollment Service API")
                        .version("1.0.0")
                        .description("API documentation for the Enrollment microservice"));
    }
}
