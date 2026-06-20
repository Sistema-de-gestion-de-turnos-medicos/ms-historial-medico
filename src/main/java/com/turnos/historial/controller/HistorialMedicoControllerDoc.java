package com.turnos.historial.controller;

import com.turnos.historial.datalizer.ApiResponse;
import com.turnos.historial.datalizer.HistorialMedicoRequest;
import com.turnos.historial.datalizer.HistorialMedicoResponse;
import com.turnos.historial.model.HistorialMedico;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

/**
 * Contrato de documentación OpenAPI / Swagger para {@link HistorialMedicoController}.
 * <p>
 * Esta interfaz no contiene lógica de negocio: su único propósito es describir, mediante
 * anotaciones de Swagger ({@code @Tag}, {@code @Operation}, {@code @ApiResponses},
 * {@code @Parameter}, {@code @RequestBody}, {@code @Schema}, {@code @Content},
 * {@code @ExampleObject}, {@code @ArraySchema}), el comportamiento de cada endpoint del
 * recurso "Historial Médico". {@link HistorialMedicoController} implementa esta interfaz,
 * manteniendo así la documentación separada de la implementación.
 */
@Tag(
        name = "Historial Médico",
        description = "Endpoints para crear, consultar, actualizar y eliminar los registros del "
                + "historial médico de los pacientes."
)
public interface HistorialMedicoControllerDoc {

    @Operation(
            summary = "Crear un historial médico",
            description = "Registra un nuevo historial médico asociado a un paciente y a un médico, "
                    + "opcionalmente vinculado a un turno previo."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Historial médico creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HistorialMedicoResponse.class),
                            examples = @ExampleObject(
                                    name = "HistorialCreado",
                                    value = "{\"success\":true,\"message\":\"Historial médico creado exitosamente\","
                                            + "\"data\":{\"id\":1,\"pacienteId\":10,\"medicoId\":5,\"turnoId\":3,"
                                            + "\"diagnostico\":\"Gripe estacional\",\"tratamiento\":\"Reposo e hidratación\","
                                            + "\"observaciones\":\"Paciente estable\",\"medicamentos\":\"Paracetamol 500mg\","
                                            + "\"fechaConsulta\":\"2026-06-10\",\"proximaConsulta\":\"2026-06-20\","
                                            + "\"estado\":\"ACTIVO\",\"createdAt\":\"2026-06-10T10:15:00\","
                                            + "\"updatedAt\":\"2026-06-10T10:15:00\"},\"timestamp\":\"2026-06-10T10:15:00\"}"
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
            )
    })
    ResponseEntity<ApiResponse<HistorialMedicoResponse>> crear(
            @RequestBody(
                    description = "Datos del historial médico a registrar",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = HistorialMedicoRequest.class),
                            examples = @ExampleObject(
                                    name = "NuevoHistorial",
                                    value = "{\"pacienteId\":10,\"medicoId\":5,\"turnoId\":3,"
                                            + "\"diagnostico\":\"Gripe estacional\",\"tratamiento\":\"Reposo e hidratación\","
                                            + "\"observaciones\":\"Paciente estable\",\"medicamentos\":\"Paracetamol 500mg\","
                                            + "\"fechaConsulta\":\"2026-06-10\",\"proximaConsulta\":\"2026-06-20\"}"
                            )
                    )
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody HistorialMedicoRequest request
    );

    @Operation(
            summary = "Obtener un historial médico por ID",
            description = "Recupera un registro de historial médico específico a partir de su identificador."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Historial médico encontrado",
                    content = @Content(schema = @Schema(implementation = HistorialMedicoResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "No existe un historial médico con el ID indicado",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ResponseEntity<ApiResponse<HistorialMedicoResponse>> obtenerPorId(
            @Parameter(description = "ID del historial médico", example = "1", required = true)
            @PathVariable Long id
    );

    @Operation(
            summary = "Obtener historiales médicos de un paciente",
            description = "Lista todos los registros de historial médico asociados a un paciente."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Historiales del paciente obtenidos exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = HistorialMedicoResponse.class))
                    )
            )
    })
    ResponseEntity<ApiResponse<List<HistorialMedicoResponse>>> obtenerPorPaciente(
            @Parameter(description = "ID del paciente", example = "10", required = true)
            @PathVariable Long pacienteId
    );

    @Operation(
            summary = "Obtener historiales médicos de un médico",
            description = "Lista todos los registros de historial médico generados por un médico en particular."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Historiales del médico obtenidos exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = HistorialMedicoResponse.class))
                    )
            )
    })
    ResponseEntity<ApiResponse<List<HistorialMedicoResponse>>> obtenerPorMedico(
            @Parameter(description = "ID del médico", example = "5", required = true)
            @PathVariable Long medicoId
    );

    @Operation(
            summary = "Obtener historiales médicos de un turno",
            description = "Lista los registros de historial médico vinculados a un turno específico."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Historiales del turno obtenidos exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = HistorialMedicoResponse.class))
                    )
            )
    })
    ResponseEntity<ApiResponse<List<HistorialMedicoResponse>>> obtenerPorTurno(
            @Parameter(description = "ID del turno", example = "3", required = true)
            @PathVariable Long turnoId
    );

    @Operation(
            summary = "Filtrar historiales de un paciente por estado",
            description = "Lista los historiales médicos de un paciente filtrados por su estado "
                    + "(ACTIVO, ARCHIVADO o ELIMINADO)."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Historiales filtrados por estado obtenidos exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = HistorialMedicoResponse.class))
                    )
            )
    })
    ResponseEntity<ApiResponse<List<HistorialMedicoResponse>>> obtenerPorPacienteYEstado(
            @Parameter(description = "ID del paciente", example = "10", required = true)
            @PathVariable Long pacienteId,
            @Parameter(
                    description = "Estado del historial médico",
                    example = "ACTIVO",
                    required = true,
                    schema = @Schema(implementation = HistorialMedico.EstadoHistorial.class)
            )
            @PathVariable HistorialMedico.EstadoHistorial estado
    );

    @Operation(
            summary = "Filtrar historiales de un paciente por rango de fechas",
            description = "Lista los historiales médicos de un paciente cuya fecha de consulta se "
                    + "encuentra entre las fechas 'desde' y 'hasta' indicadas (formato ISO yyyy-MM-dd)."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Historiales en el rango de fechas obtenidos exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = HistorialMedicoResponse.class))
                    )
            )
    })
    ResponseEntity<ApiResponse<List<HistorialMedicoResponse>>> obtenerPorRangoFechas(
            @Parameter(description = "ID del paciente", example = "10", required = true)
            @PathVariable Long pacienteId,
            @Parameter(description = "Fecha desde (inclusive)", example = "2026-01-01", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @Parameter(description = "Fecha hasta (inclusive)", example = "2026-06-30", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    );

    @Operation(
            summary = "Actualizar un historial médico",
            description = "Actualiza la información de un historial médico existente identificado por su ID."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Historial médico actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = HistorialMedicoResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Error de validación en los datos enviados",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "No existe un historial médico con el ID indicado",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ResponseEntity<ApiResponse<HistorialMedicoResponse>> actualizar(
            @Parameter(description = "ID del historial médico a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @RequestBody(
                    description = "Nuevos datos del historial médico",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = HistorialMedicoRequest.class),
                            examples = @ExampleObject(
                                    name = "ActualizarHistorial",
                                    value = "{\"pacienteId\":10,\"medicoId\":5,\"turnoId\":3,"
                                            + "\"diagnostico\":\"Gripe estacional - control\",\"tratamiento\":\"Continuar reposo\","
                                            + "\"observaciones\":\"Mejoría notable\",\"medicamentos\":\"Paracetamol 500mg\","
                                            + "\"fechaConsulta\":\"2026-06-10\",\"proximaConsulta\":\"2026-06-25\"}"
                            )
                    )
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody HistorialMedicoRequest request
    );

    @Operation(
            summary = "Cambiar el estado de un historial médico",
            description = "Actualiza únicamente el estado (ACTIVO, ARCHIVADO o ELIMINADO) de un "
                    + "historial médico existente."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Estado actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = HistorialMedicoResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "No existe un historial médico con el ID indicado",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ResponseEntity<ApiResponse<HistorialMedicoResponse>> cambiarEstado(
            @Parameter(description = "ID del historial médico", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(
                    description = "Nuevo estado del historial médico",
                    example = "ARCHIVADO",
                    required = true,
                    schema = @Schema(implementation = HistorialMedico.EstadoHistorial.class)
            )
            @RequestParam HistorialMedico.EstadoHistorial estado
    );

    @Operation(
            summary = "Eliminar un historial médico",
            description = "Elimina de forma permanente un registro de historial médico. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Historial médico eliminado exitosamente",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "El usuario autenticado no tiene el rol ADMIN",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "No existe un historial médico con el ID indicado",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ResponseEntity<ApiResponse<Void>> eliminar(
            @Parameter(description = "ID del historial médico a eliminar", example = "1", required = true)
            @PathVariable Long id
    );
}
