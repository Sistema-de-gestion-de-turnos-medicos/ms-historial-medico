package com.turnos.historial.datalizer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HistorialMedicoRequest {

    @NotNull(message = "El ID del paciente es obligatorio")
    private Long pacienteId;

    @NotNull(message = "El ID del médico es obligatorio")
    private Long medicoId;

    private Long turnoId;

    @NotBlank(message = "El diagnóstico es obligatorio")
    private String diagnostico;

    private String tratamiento;

    private String observaciones;

    private String medicamentos;

    @NotNull(message = "La fecha de consulta es obligatoria")
    private LocalDate fechaConsulta;

    private LocalDate proximaConsulta;
}
