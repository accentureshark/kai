package org.shark.kai.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("KAI - Knowledge Access & Identity API")
                        .description("REST API for managing users, organizations, areas, and roles")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("KAI Development Team")
                                .email("team@accenture.com")
                                .url("https://github.com/accentureshark/kai"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local development server"),
                        new Server().url("https://api.kai.com").description("Production server")
                ));
    }
}