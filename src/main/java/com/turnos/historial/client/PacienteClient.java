package com.turnos.historial.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Cliente Feign para comunicación con el microservicio de Pacientes.
 * Permite validar la existencia de un paciente antes de registrar su historial.
 */
@FeignClient(name = "ms-pacientes", url = "${feign.client.ms-pacientes.url}")
public interface PacienteClient {

    @GetMapping("/api/v1/pacientes/{id}")
    PacienteResponse obtenerPorId(@PathVariable("id") Long id);
}
