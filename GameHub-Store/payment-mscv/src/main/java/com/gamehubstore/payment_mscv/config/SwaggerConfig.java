package com.gamehubstore.payment_mscv.config; // 👈 Paquete oficial de tu mscv de pagos

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
        // Habilita la interfaz visual con el candado de autenticación para el procesamiento de pagos
        return new OpenAPI()
                .info(new Info()
                        .title("GameHub Store - API Procesamiento de Pagos")
                        .version("1.0")
                        .description("Microservicio encargado de la integración con pasarelas transaccionales, emisión de comprobantes financieros y gestión de reembolsos."))
                .components(new Components().addSecuritySchemes("bearer-jwt",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
