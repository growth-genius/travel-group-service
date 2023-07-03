package com.gg.tgather.travelgroupservice.infra.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private final Info info = new Info().version("v1.0.1").title("Travel-Group").description("Travel Group Info");

    @Bean
    public OpenAPI openAPI() {
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("jwtAuth");
        Components components = new Components().addSecuritySchemes("jwtAuth",
                new SecurityScheme().name("jwtAuth").type(Type.HTTP).scheme("bearer").bearerFormat("JWT"));

        return new OpenAPI().info(info).addSecurityItem(securityRequirement).components(components).addServersItem(new Server().url("http://www.tpsg.co.kr:8000/api/travel-group"));
    }
}
