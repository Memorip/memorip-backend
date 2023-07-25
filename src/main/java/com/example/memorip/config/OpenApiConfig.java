package com.example.memorip.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Memorip API Document")
                .version("v1.0.0")
                .description("Memorip의 API 명세서입니다.");


        SecurityScheme auth = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.COOKIE).name("accessToken");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("basicAuth");

        Components components = new Components().addSecuritySchemes("basicAuth", auth);


        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);

    }
}