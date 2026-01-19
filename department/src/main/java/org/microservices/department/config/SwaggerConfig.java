package org.microservices.department.config;

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
                .servers(List.of(new Server().url("/department")
                        .description("Department Service API Server")))
                .info(new Info()
                        .title("Department Service API")
                        .version("1.0.0")
                        .description("API documentation for the Department microservice"));
    }
}
