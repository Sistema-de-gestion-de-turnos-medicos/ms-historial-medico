package com.turnos.historial.controller;

import com.turnos.historial.datalizer.AdjuntoRequest;
import com.turnos.historial.datalizer.ApiResponse;
import com.turnos.historial.model.Adjunto;
import com.turnos.historial.service.AdjuntoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/adjuntos")
@RequiredArgsConstructor
public class AdjuntoController implements AdjuntoControllerDoc {

    private final AdjuntoService adjuntoService;

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<ApiResponse<Adjunto>> agregar(@Valid @RequestBody AdjuntoRequest request) {
        Adjunto adjunto = adjuntoService.agregar(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Adjunto agregado exitosamente", adjunto));
    }

    @Override
    @GetMapping("/historial/{historialId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'PACIENTE')")
    public ResponseEntity<ApiResponse<List<Adjunto>>> obtenerPorHistorial(@PathVariable Long historialId) {
        List<Adjunto> adjuntos = adjuntoService.obtenerPorHistorial(historialId);
        return ResponseEntity.ok(ApiResponse.ok("Adjuntos obtenidos", adjuntos));
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        adjuntoService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Adjunto eliminado exitosamente", null));
    }
}
