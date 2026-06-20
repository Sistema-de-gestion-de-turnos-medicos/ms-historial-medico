package com.turnos.historial.controller;

import com.turnos.historial.datalizer.ApiResponse;
import com.turnos.historial.datalizer.HistorialMedicoRequest;
import com.turnos.historial.datalizer.HistorialMedicoResponse;
import com.turnos.historial.model.HistorialMedico;
import com.turnos.historial.service.HistorialMedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/historial-medico")
@RequiredArgsConstructor
@Slf4j
public class HistorialMedicoController implements HistorialMedicoControllerDoc {

    private final HistorialMedicoService historialMedicoService;

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<ApiResponse<HistorialMedicoResponse>> crear(
            @Valid @RequestBody HistorialMedicoRequest request) {
        log.info("POST /api/v1/historial-medico - Creando registro");
        HistorialMedicoResponse response = historialMedicoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Historial médico creado exitosamente", response));
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'PACIENTE')")
    public ResponseEntity<ApiResponse<HistorialMedicoResponse>> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/v1/historial-medico/{}", id);
        HistorialMedicoResponse response = historialMedicoService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.ok("Historial médico encontrado", response));
    }

    @Override
    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'PACIENTE')")
    public ResponseEntity<ApiResponse<List<HistorialMedicoResponse>>> obtenerPorPaciente(
            @PathVariable Long pacienteId) {
        log.info("GET /api/v1/historial-medico/paciente/{}", pacienteId);
        List<HistorialMedicoResponse> lista = historialMedicoService.obtenerPorPaciente(pacienteId);
        return ResponseEntity.ok(ApiResponse.ok("Historiales del paciente obtenidos", lista));
    }

    @Override
    @GetMapping("/medico/{medicoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<ApiResponse<List<HistorialMedicoResponse>>> obtenerPorMedico(
            @PathVariable Long medicoId) {
        log.info("GET /api/v1/historial-medico/medico/{}", medicoId);
        List<HistorialMedicoResponse> lista = historialMedicoService.obtenerPorMedico(medicoId);
        return ResponseEntity.ok(ApiResponse.ok("Historiales del médico obtenidos", lista));
    }

    @Override
    @GetMapping("/turno/{turnoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<ApiResponse<List<HistorialMedicoResponse>>> obtenerPorTurno(
            @PathVariable Long turnoId) {
        log.info("GET /api/v1/historial-medico/turno/{}", turnoId);
        List<HistorialMedicoResponse> lista = historialMedicoService.obtenerPorTurno(turnoId);
        return ResponseEntity.ok(ApiResponse.ok("Historiales del turno obtenidos", lista));
    }

    @Override
    @GetMapping("/paciente/{pacienteId}/estado/{estado}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<ApiResponse<List<HistorialMedicoResponse>>> obtenerPorPacienteYEstado(
            @PathVariable Long pacienteId,
            @PathVariable HistorialMedico.EstadoHistorial estado) {
        List<HistorialMedicoResponse> lista = historialMedicoService.obtenerPorPacienteYEstado(pacienteId, estado);
        return ResponseEntity.ok(ApiResponse.ok("Historiales filtrados por estado", lista));
    }

    @Override
    @GetMapping("/paciente/{pacienteId}/rango")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'PACIENTE')")
    public ResponseEntity<ApiResponse<List<HistorialMedicoResponse>>> obtenerPorRangoFechas(
            @PathVariable Long pacienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        List<HistorialMedicoResponse> lista = historialMedicoService.obtenerPorPacienteYRangoFechas(pacienteId, desde, hasta);
        return ResponseEntity.ok(ApiResponse.ok("Historiales en rango de fechas obtenidos", lista));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<ApiResponse<HistorialMedicoResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody HistorialMedicoRequest request) {
        log.info("PUT /api/v1/historial-medico/{}", id);
        HistorialMedicoResponse response = historialMedicoService.actualizar(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Historial médico actualizado exitosamente", response));
    }

    @Override
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<ApiResponse<HistorialMedicoResponse>> cambiarEstado(
            @PathVariable Long id,
            @RequestParam HistorialMedico.EstadoHistorial estado) {
        HistorialMedicoResponse response = historialMedicoService.cambiarEstado(id, estado);
        return ResponseEntity.ok(ApiResponse.ok("Estado actualizado exitosamente", response));
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/historial-medico/{}", id);
        historialMedicoService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Historial médico eliminado exitosamente", null));
    }
}
