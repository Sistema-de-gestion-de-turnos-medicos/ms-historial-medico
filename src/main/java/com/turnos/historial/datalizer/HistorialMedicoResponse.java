package com.turnos.historial.datalizer;

import com.turnos.historial.model.HistorialMedico;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class HistorialMedicoResponse {

    private Long id;
    private Long pacienteId;
    private Long medicoId;
    private Long turnoId;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;
    private String medicamentos;
    private LocalDate fechaConsulta;
    private LocalDate proximaConsulta;
    private HistorialMedico.EstadoHistorial estado;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static HistorialMedicoResponse fromEntity(HistorialMedico entity) {
        return HistorialMedicoResponse.builder()
                .id(entity.getId())
                .pacienteId(entity.getPacienteId())
                .medicoId(entity.getMedicoId())
                .turnoId(entity.getTurnoId())
                .diagnostico(entity.getDiagnostico())
                .tratamiento(entity.getTratamiento())
                .observaciones(entity.getObservaciones())
                .medicamentos(entity.getMedicamentos())
                .fechaConsulta(entity.getFechaConsulta())
                .proximaConsulta(entity.getProximaConsulta())
                .estado(entity.getEstado())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
