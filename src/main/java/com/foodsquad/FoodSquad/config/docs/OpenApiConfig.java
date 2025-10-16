package com.foodsquad.FoodSquad.config.docs;

import com.foodsquad.FoodSquad.config.web.TenantFilter;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.parameters.QueryParameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.foodsquad.FoodSquad.config.utils.Constant.CLIENT_API_BASE_PACKAGE;
import static com.foodsquad.FoodSquad.config.web.TenantFilter.TENANT_HEADER;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearer-key";
    private static final String STORE_SLUG_PARAM = "storeSlug";

    @Bean
    public OpenAPI baseOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FoodSquad API")
                        .version("1.0.0")
                        .description("API documentation for FoodSquad - A comprehensive food delivery and restaurant management system"))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))
                        .addParameters(TENANT_HEADER, new HeaderParameter()
                                .name(TENANT_HEADER)
                                .description("Tenant identifier")
                                .required(false)
                                .example("tenant_1")))
                .addTagsItem(new Tag()
                        .name("1. Authentication")
                        .description("Authentication and authorization endpoints"))
                .addTagsItem(new Tag()
                        .name("2. Token Management")
                        .description("JWT token management and validation"))
                
                .addTagsItem(new Tag()
                        .name("3. User Management")
                        .description("User account and profile management"))
                .addTagsItem(new Tag()
                        .name("Customer Management")
                        .description("Customer Management API"))
                
                .addTagsItem(new Tag()
                        .name("5. Product Management")
                        .description("Menu Item Management API"))
                .addTagsItem(new Tag()
                        .name("7. Category Management")
                        .description("Category Management API"))
                .addTagsItem(new Tag()
                        .name("Supplement Groups")
                        .description("API for managing supplement groups"))
                .addTagsItem(new Tag()
                        .name("Supplement Options")
                        .description("API for managing supplement options"))
                
                .addTagsItem(new Tag()
                        .name("4. Order Management")
                        .description("Order Management API"))
                .addTagsItem(new Tag()
                        .name("Client Order Management")
                        .description("Client Order Management API"))
                .addTagsItem(new Tag()
                        .name("Order Status")
                        .description("APIs for managing order statuses"))
                .addTagsItem(new Tag()
                        .name("9. Invoice Management")
                        .description("Invoice Management API"))
                
                .addTagsItem(new Tag()
                        .name("8. Menu Management")
                        .description("Menu Management API"))
                .addTagsItem(new Tag()
                        .name("6. Review Management")
                        .description("Review Management API"))
                .addTagsItem(new Tag()
                        .name("13. Promotion Management")
                        .description("Gestion des promotions"))
                
                .addTagsItem(new Tag()
                        .name("10. Media Management")
                        .description("Media Management API"))
                .addTagsItem(new Tag()
                        .name("11. Currency Management")
                        .description("Currency Management API"))
                .addTagsItem(new Tag()
                        .name("Store Management")
                        .description("APIs for managing stores"))
                .addTagsItem(new Tag()
                        .name("Timbre Management")
                        .description("APIs for managing timbres"))
                .addTagsItem(new Tag()
                        .name("Custom Attribute Management")
                        .description("APIs for managing custom attributes"))
                .addTagsItem(new Tag()
                        .name("Custom Attribute Types")
                        .description("API for listing supported custom attribute types"))
                .addTagsItem(new Tag()
                        .name("Country Management")
                        .description("APIs for managing countries"))
                .addTagsItem(new Tag()
                        .name("Languages")
                        .description("API endpoints for managing languages"))
                .addTagsItem(new Tag()
                        .name("Activity Sector Management")
                        .description("APIs for managing activity sectors"))
          .addTagsItem(new Tag()
                .name("Customer Management")
                .description("APIs for managing customers"));
    }

    @Bean
    public GroupedOpenApi clientApi() {
        return GroupedOpenApi.builder()
                .group("client")
                .packagesToScan(CLIENT_API_BASE_PACKAGE)
                .addOpenApiCustomizer(openApi -> {
                    openApi.getPaths().forEach((path, pathItem) ->
                            pathItem.readOperations().forEach(operation -> {
                                operation.addParametersItem(new QueryParameter()
                                        .name("storeSlug")
                                        .description("Store identifier (slug)")
                                        .required(false)
                                        .example("my-store-123"));
                            })
                    );
                })
                .build();
    }


    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("Admin APIs")
                .packagesToScan(
                        "com.foodsquad.FoodSquad.controller.admin",
                        "com.foodsquad.FoodSquad.controller"
                )
                .pathsToMatch(
                        "/api/customers/**" ,
                        "/api/auth/**",
                        "/api/token/**",
                        "/api/users/**",
                        "/api/products/**",
                        "/api/categories/**",
                        "/api/orders/**",
                        "/api/invoices/**",
                        "/api/promotions/**",
                        "/api/reviews/**",
                        "/api/menus/**",
                        "/api/media/**",
                        "/api/currencies/**",
                        "/api/stores/**",
                        "/api/timbres/**",
                        "/api/attribute-values/**",
                        "/api/activity-sectors/**",
                        "/api/custom-attributes/**",
                        "/api/custom-attribute-types/**",
                        "/api/countries/**",
                        "/api/languages/**",
                        "/api/supplement-groups/**",
                        "/api/product-options/**"
                )
                .addOpenApiCustomizer(openApi -> {
                    openApi.addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));

                    openApi.getPaths().forEach((path, pathItem) ->
                            pathItem.readOperations().forEach(operation -> {
                                operation.addParametersItem(new HeaderParameter()
                                        .name(TENANT_HEADER)
                                        .description("Tenant identifier")
                                        .required(true)
                                        .example("admin_tenant"));
                            })
                    );
                })
                .build();
    }
}
