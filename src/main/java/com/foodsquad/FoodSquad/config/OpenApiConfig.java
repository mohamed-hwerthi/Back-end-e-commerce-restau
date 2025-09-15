package com.foodsquad.FoodSquad.config;

import com.foodsquad.FoodSquad.config.db.TenantFilter;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Easy API")
                        .version("1.0")
                        .description("API documentation for Easy application"))
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"))
                        .addParameters("X-TenantID", new HeaderParameter()
                                .name("X-TenantID")
                                .description("Tenant identifier")
                                .required(false)
                                .example("tenant_1")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"))
                .addTagsItem(new Tag().name("1. Token Management").description("Token Management API"))
                .addTagsItem(new Tag().name("2. Authentication").description("Authentication API"))
                .addTagsItem(new Tag().name("3. User Management").description("User Management API"))
                .addTagsItem(new Tag().name("4. Order Management").description("Order Management API"))
                .addTagsItem(new Tag().name("5. Menu Item Management").description("Menu Item Management API"))
                .addTagsItem(new Tag().name("6. Review Management").description("Review Item Management API"))
                .addTagsItem(new Tag().name("7. Category Management").description("Category Management API"))
                .addTagsItem(new Tag().name("8. Menu Management").description("Menu Management API"))
                .addTagsItem(new Tag().name("9. InvoiceManagement").description("Invoice  Management API"))
                .addTagsItem(new Tag().name("10. Media Management").description("Media Management API"))
                .addTagsItem(new Tag().name("11. Currency Management").description("Currency Management API"))
                .addTagsItem(new Tag().name("12. Timbre Management").description("APIs for managing timbres"))
                .addTagsItem(new Tag().name("13. Promotion Management").description("APIs for managing promotions"));


    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/api/**")
                .addOpenApiCustomizer(openApi -> {
                    var excludedPaths = List.of(
                            "/api/stores",
                            "/api/auth/owner/sign-in",
                            "/api/activity-sectors"
                    );

                    openApi.getPaths().forEach((path, pathItem) -> {
                        boolean isExcluded = excludedPaths.stream().anyMatch(path::startsWith);
                        if (!isExcluded) {
                            pathItem.readOperations().forEach(operation -> {
                                operation.addParametersItem(new HeaderParameter()
                                        .name(TenantFilter.TENANT_HEADER)
                                        .description("Tenant identifier")
                                        .required(false)
                                        .example("tenant_1"));
                            });
                        }
                    });
                })
                .build();
    }


}
