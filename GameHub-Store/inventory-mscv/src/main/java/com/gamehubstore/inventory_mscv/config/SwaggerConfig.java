package com.gamehubstore.inventory_mscv.config; // 👈 Paquete oficial de tu microservicio de inventario

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
        // Registra el esquema "bearer-jwt" para habilitar el botón 'Authorize' (Candado) en este microservicio
        return new OpenAPI()
                .info(new Info()
                        .title("GameHub Store - API Inventario y Stock") // 👈 Título personalizado
                        .version("1.0")
                        .description("Microservicio encargado del control de stock disponible, stock reservado y auditoría de movimientos de bodega."))
                .components(new Components().addSecuritySchemes("bearer-jwt",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
