package com.turnos.historial.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Cliente Feign para comunicación con el microservicio de Médicos.
 * Permite validar la existencia de un médico antes de registrar el historial.
 */
@FeignClient(name = "ms-medicos", url = "${feign.client.ms-medicos.url}")
public interface MedicoClient {

    @GetMapping("/api/v1/medicos/{id}")
    MedicoResponse obtenerPorId(@PathVariable("id") Long id);
}
