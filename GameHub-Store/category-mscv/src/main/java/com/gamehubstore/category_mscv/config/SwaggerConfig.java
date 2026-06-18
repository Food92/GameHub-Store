package com.gamehubstore.category_mscv.config; // 👈 Paquete oficial de tu mscv de categorías

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi() {
        // Registra el esquema "bearer-jwt" para habilitar el candado de seguridad en la UI de Categorías
        return new OpenAPI()
                .info(new Info()
                        .title("GameHub Store - API Categorías") // 👈 Título personalizado
                        .version("1.0")
                        .description("Microservicio encargado de la clasificación, géneros y etiquetas del catálogo de videojuegos."))
                .components(new Components().addSecuritySchemes("bearer-jwt",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
