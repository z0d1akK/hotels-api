package com.zodiakk.hotelsapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI hotelsOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotels API")
                        .description("RESTful API for hotel property view operations")
                        .version("v1")
                        .contact(new Contact()
                                .name("Artem")
                                .email("art.linenko@gmail.com")));
    }
}
