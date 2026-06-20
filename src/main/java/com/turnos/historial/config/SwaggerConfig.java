package com.turnos.historial.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI / Swagger UI para el microservicio de Historial Médico.
 * <p>
 * Define la metadata general de la documentación (título, descripción, contacto, licencia)
 * y el esquema de seguridad JWT (Bearer Token) que protege los endpoints del servicio,
 * de manera que pueda probarse la API directamente desde Swagger UI usando el botón
 * "Authorize".
 * <p>
 * Una vez levantada la aplicación, la documentación interactiva queda disponible en:
 * <ul>
 *     <li>Swagger UI: <code>http://localhost:8090/swagger-ui.html</code></li>
 *     <li>Especificación OpenAPI (JSON): <code>http://localhost:8090/v3/api-docs</code></li>
 * </ul>
 */
@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8090")
                                .description("Servidor local de desarrollo")
                ))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme()))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }

    private Info apiInfo() {
        return new Info()
                .title("API - Historial Médico")
                .description("Microservicio encargado de la gestión del historial médico de los pacientes "
                        + "dentro del Sistema de Gestión de Turnos Médicos. Permite crear, consultar, "
                        + "actualizar y eliminar historiales médicos, así como administrar los archivos "
                        + "adjuntos asociados a cada historial.")
                .version("1.0.0")
                .contact(new Contact()
                        .name("Equipo Turnos Médicos")
                        .email("soporte@turnosmedicos.com"))
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0"));
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Token JWT entregado por el servicio central de autenticación. "
                        + "Formato: Bearer {token}");
    }
}
