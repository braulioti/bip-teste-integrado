package io.brau.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI beneficioOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BIP Teste Integrado API")
                        .description("API Spring Boot para CRUD e transferencia de beneficios.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Braulio Figueiredo")
                                .url("https://brau.io"))
                        .license(new License()
                                .name("Uso interno para avaliacao tecnica")));
    }
}
