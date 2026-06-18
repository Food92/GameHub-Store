package com.gamehubstore.shipping_mscv.config; // 👈 Paquete oficial de tu mscv de envíos

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
        // Habilita la interfaz visual con el candado de autenticación para la gestión de despacho logístico
        return new OpenAPI()
                .info(new Info()
                        .title("GameHub Store - API Envíos y Logística")
                        .version("1.0")
                        .description("Microservicio encargado del seguimiento de paquetes (Tracking), asignación de transportistas y control de direcciones de despacho."))
                .components(new Components().addSecuritySchemes("bearer-jwt",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
