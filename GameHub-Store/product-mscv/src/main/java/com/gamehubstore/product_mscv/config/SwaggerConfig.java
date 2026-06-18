package com.gamehubstore.product_mscv.config; // 👈 Paquete oficial de tu mscv de productos

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
        // Habilita la interfaz visual con el candado de autenticación para la gestión de videojuegos
        return new OpenAPI()
                .info(new Info()
                        .title("GameHub Store - API Catálogo de Productos")
                        .version("1.0")
                        .description("Microservicio encargado de la persistencia de videojuegos, especificaciones técnicas, precios base e integración con almacenes."))
                .components(new Components().addSecuritySchemes("bearer-jwt",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
