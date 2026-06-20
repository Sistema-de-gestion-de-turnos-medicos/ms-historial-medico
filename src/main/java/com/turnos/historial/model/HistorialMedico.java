package com.turnos.historial.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_medico")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del paciente es obligatorio")
    @Column(name = "paciente_id", nullable = false)
    private Long pacienteId;

    @NotNull(message = "El ID del médico es obligatorio")
    @Column(name = "medico_id", nullable = false)
    private Long medicoId;

    @Column(name = "turno_id")
    private Long turnoId;

    @NotBlank(message = "El diagnóstico es obligatorio")
    @Column(name = "diagnostico", nullable = false, columnDefinition = "TEXT")
    private String diagnostico;

    @Column(name = "tratamiento", columnDefinition = "TEXT")
    private String tratamiento;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "medicamentos", columnDefinition = "TEXT")
    private String medicamentos;

    @NotNull(message = "La fecha de consulta es obligatoria")
    @Column(name = "fecha_consulta", nullable = false)
    private LocalDate fechaConsulta;

    @Column(name = "proxima_consulta")
    private LocalDate proximaConsulta;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    @Builder.Default
    private EstadoHistorial estado = EstadoHistorial.ACTIVO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum EstadoHistorial {
        ACTIVO, ARCHIVADO, ELIMINADO
    }
}
