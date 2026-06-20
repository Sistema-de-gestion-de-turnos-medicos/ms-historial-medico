package com.turnos.historial.controller;

import com.turnos.historial.datalizer.AdjuntoRequest;
import com.turnos.historial.datalizer.ApiResponse;
import com.turnos.historial.model.Adjunto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Contrato de documentación OpenAPI / Swagger para {@link AdjuntoController}.
 * <p>
 * Esta interfaz no contiene lógica de negocio: describe, mediante anotaciones de Swagger
 * ({@code @Tag}, {@code @Operation}, {@code @ApiResponses}, {@code @Parameter},
 * {@code @RequestBody}, {@code @Schema}, {@code @Content}, {@code @ExampleObject},
 * {@code @ArraySchema}), el comportamiento de cada endpoint del recurso "Adjuntos".
 * {@link AdjuntoController} implementa esta interfaz, manteniendo la documentación
 * separada de la implementación.
 */
@Tag(
        name = "Adjuntos",
        description = "Endpoints para administrar los archivos adjuntos (estudios, imágenes, recetas, "
                + "etc.) asociados a un historial médico."
)
public interface AdjuntoControllerDoc {

    @Operation(
            summary = "Agregar un adjunto a un historial médico",
            description = "Registra un nuevo archivo adjunto (por ejemplo, un estudio o una imagen) "
                    + "vinculado a un historial médico existente."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Adjunto agregado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Adjunto.class),
                            examples = @ExampleObject(
                                    name = "AdjuntoCreado",
                                    value = "{\"success\":true,\"message\":\"Adjunto agregado exitosamente\","
                                            + "\"data\":{\"id\":1,\"nombreArchivo\":\"radiografia_torax.png\","
                                            + "\"urlArchivo\":\"https://storage.turnosmedicos.com/adjuntos/1.png\","
                                            + "\"tipoArchivo\":\"image/png\",\"tamanoBytes\":204800,"
                                            + "\"createdAt\":\"2026-06-10T10:20:00\"},\"timestamp\":\"2026-06-10T10:20:00\"}"
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Error de validación en los datos enviados",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "No autenticado. El token JWT no fue enviado o no es válido",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "El usuario autenticado no tiene el rol requerido (ADMIN o MEDICO)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "No existe el historial médico indicado",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ResponseEntity<ApiResponse<Adjunto>> agregar(
            @RequestBody(
                    description = "Datos del adjunto a registrar",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AdjuntoRequest.class),
                            examples = @ExampleObject(
                                    name = "NuevoAdjunto",
                                    value = "{\"historialId\":1,\"nombreArchivo\":\"radiografia_torax.png\","
                                            + "\"urlArchivo\":\"https://storage.turnosmedicos.com/adjuntos/1.png\","
                                            + "\"tipoArchivo\":\"image/png\",\"tamanoBytes\":204800}"
                            )
                    )
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody AdjuntoRequest request
    );

    @Operation(
            summary = "Obtener los adjuntos de un historial médico",
            description = "Lista todos los archivos adjuntos asociados a un historial médico específico."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Adjuntos obtenidos exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Adjunto.class))
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "No existe el historial médico indicado",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ResponseEntity<ApiResponse<List<Adjunto>>> obtenerPorHistorial(
            @Parameter(description = "ID del historial médico", example = "1", required = true)
            @PathVariable Long historialId
    );

    @Operation(
            summary = "Eliminar un adjunto",
            description = "Elimina de forma permanente un archivo adjunto de un historial médico."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Adjunto eliminado exitosamente",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "El usuario autenticado no tiene el rol requerido (ADMIN o MEDICO)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "No existe un adjunto con el ID indicado",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ResponseEntity<ApiResponse<Void>> eliminar(
            @Parameter(description = "ID del adjunto a eliminar", example = "1", required = true)
            @PathVariable Long id
    );
}
