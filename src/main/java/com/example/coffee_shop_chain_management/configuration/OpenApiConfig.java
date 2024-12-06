package com.example.coffee_shop_chain_management.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Coffee Shop Chain Management")
                        .description("APIs for Coffee Shop Chain Management")
                        .version("1.0.0"));
    }
}

