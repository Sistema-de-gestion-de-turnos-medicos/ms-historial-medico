package com.turnos.historial.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "adjuntos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Adjunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El historial médico es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "historial_id", nullable = false)
    private HistorialMedico historialMedico;

    @NotBlank(message = "El nombre del archivo es obligatorio")
    @Column(name = "nombre_archivo", nullable = false)
    private String nombreArchivo;

    @NotBlank(message = "La URL del archivo es obligatoria")
    @Column(name = "url_archivo", nullable = false)
    private String urlArchivo;

    @Column(name = "tipo_archivo")
    private String tipoArchivo;

    @Column(name = "tamano_bytes")
    private Long tamanoBytes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
