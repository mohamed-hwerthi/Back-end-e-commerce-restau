package com.foodsquad.FoodSquad.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CookieValue;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Food Squad API")
                        .version("1.0")
                        .description("API documentation for Food Squad application"))
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"))
                .addTagsItem(new Tag().name("1. Token Management").description("Token Management API"))
                .addTagsItem(new Tag().name("2. Authentication").description("Authentication API"))
                .addTagsItem(new Tag().name("3. User Management").description("User Management API"))
                .addTagsItem(new Tag().name("4. Order Management").description("Order Management API"))
                .addTagsItem(new Tag().name("5. Menu Item Management").description("Menu Item Management API"))
                .addTagsItem(new Tag().name("6. Review Management").description("Review Item Management API"))
                .addTagsItem(new Tag().name("7. Category Management").description("Category Management API"))
                .addTagsItem(new Tag().name("8. Menu Management").description("Menu Management API"))
                .addTagsItem(new Tag().name("9. Invoice  Management").description("Invoice  Management API"));

    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/api/**")
                .build();
    }
}
