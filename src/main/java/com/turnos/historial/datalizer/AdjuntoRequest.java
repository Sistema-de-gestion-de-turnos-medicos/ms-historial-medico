package com.turnos.historial.datalizer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdjuntoRequest {

    @NotNull(message = "El ID del historial es obligatorio")
    private Long historialId;

    @NotBlank(message = "El nombre del archivo es obligatorio")
    private String nombreArchivo;

    @NotBlank(message = "La URL del archivo es obligatoria")
    private String urlArchivo;

    private String tipoArchivo;

    private Long tamanoBytes;
}
