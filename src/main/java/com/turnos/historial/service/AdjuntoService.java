package com.turnos.historial.service;

import com.turnos.historial.datalizer.AdjuntoRequest;
import com.turnos.historial.model.Adjunto;
import com.turnos.historial.model.HistorialMedico;
import com.turnos.historial.repository.AdjuntoRepository;
import com.turnos.historial.repository.HistorialMedicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdjuntoService {

    private final AdjuntoRepository adjuntoRepository;
    private final HistorialMedicoRepository historialMedicoRepository;

    public Adjunto agregar(AdjuntoRequest request) {
        log.info("Agregando adjunto al historial ID: {}", request.getHistorialId());

        HistorialMedico historial = historialMedicoRepository.findById(request.getHistorialId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Historial médico no encontrado con ID: " + request.getHistorialId()));

        Adjunto adjunto = Adjunto.builder()
                .historialMedico(historial)
                .nombreArchivo(request.getNombreArchivo())
                .urlArchivo(request.getUrlArchivo())
                .tipoArchivo(request.getTipoArchivo())
                .tamanoBytes(request.getTamanoBytes())
                .build();

        return adjuntoRepository.save(adjunto);
    }

    @Transactional(readOnly = true)
    public List<Adjunto> obtenerPorHistorial(Long historialId) {
        return adjuntoRepository.findByHistorialMedicoId(historialId);
    }

    public void eliminar(Long id) {
        log.info("Eliminando adjunto ID: {}", id);
        if (!adjuntoRepository.existsById(id)) {
            throw new EntityNotFoundException("Adjunto no encontrado con ID: " + id);
        }
        adjuntoRepository.deleteById(id);
    }
}
