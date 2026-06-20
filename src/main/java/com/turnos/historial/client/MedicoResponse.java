package com.turnos.historial.client;

import lombok.Data;

@Data
public class MedicoResponse {
    private Long id;
    private String nombre;
    private String apellido;
    private String especialidad;
    private String matricula;
}
