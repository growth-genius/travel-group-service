package com.gg.tgather.travelgroupservice.infra.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    Info info = new Info().version("v1.0.1").title("Travel-Group").description("Travel Group Info");

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder().group("group1").pathsToMatch("/travel-group/**").build();
    }


    @Bean
    public OpenAPI openAPI() {

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("jwtAuth");

        Components components = new Components().addSecuritySchemes("jwtAuth",
            new SecurityScheme().name("jwtAuth").type(Type.HTTP).scheme("bearer").bearerFormat("JWT"));

        return new OpenAPI().info(info).addSecurityItem(securityRequirement).components(components);
    }
}