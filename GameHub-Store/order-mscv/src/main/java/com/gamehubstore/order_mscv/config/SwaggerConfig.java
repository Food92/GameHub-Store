package com.gamehubstore.order_mscv.config; // 👈 Cambiado al paquete oficial de tus órdenes

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
        // Registra el esquema "bearer-jwt": agrega el botón 'Authorize' en la UI de Órdenes
        return new OpenAPI()
                .info(new Info()
                        .title("GameHub Store - API Órdenes y Pedidos") // 👈 Título personalizado de tu tienda
                        .version("1.0")
                        .description("Microservicio encargado del ciclo de vida de compras, transacciones y asignación de despachos."))
                .components(new Components().addSecuritySchemes("bearer-jwt",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
