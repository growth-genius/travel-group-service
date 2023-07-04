package com.gg.tgather.travelgroupservice.infra.config;

import com.gg.tgather.travelgroupservice.infra.properties.CustomProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final CustomProperties customProperties;
    private final Info info = new Info().version("v1.0.1").title("Travel-Group").description("Travel Group Info");

    @Bean
    public OpenAPI openAPI() {
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("jwtAuth");
        Components components = new Components().addSecuritySchemes("jwtAuth",
            new SecurityScheme().name("jwtAuth").type(Type.HTTP).scheme("bearer").bearerFormat("JWT"));

        return new OpenAPI().info(info).addSecurityItem(securityRequirement).components(components).servers(getServers());
    }

    private List<Server> getServers() {
        List<Server> servers = new ArrayList<>();
        for (String server : customProperties.getServers()) {
            servers.add(new Server().url(server));
        }
        return servers;
    }
    
}
