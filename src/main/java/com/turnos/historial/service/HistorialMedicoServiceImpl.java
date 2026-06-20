package com.turnos.historial.service;

import com.turnos.historial.client.MedicoClient;
import com.turnos.historial.client.MedicoResponse;
import com.turnos.historial.client.PacienteClient;
import com.turnos.historial.client.PacienteResponse;
import com.turnos.historial.datalizer.HistorialMedicoRequest;
import com.turnos.historial.datalizer.HistorialMedicoResponse;
import com.turnos.historial.model.HistorialMedico;
import com.turnos.historial.repository.HistorialMedicoRepository;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class HistorialMedicoServiceImpl implements HistorialMedicoService {

    private final HistorialMedicoRepository historialMedicoRepository;
    private final PacienteClient pacienteClient;
    private final MedicoClient medicoClient;

    @Override
    public HistorialMedicoResponse crear(HistorialMedicoRequest request) {
        log.info("Creando historial médico para paciente ID: {}", request.getPacienteId());


        validarPaciente(request.getPacienteId());

        validarMedico(request.getMedicoId());

        HistorialMedico historial = HistorialMedico.builder()
                .pacienteId(request.getPacienteId())
                .medicoId(request.getMedicoId())
                .turnoId(request.getTurnoId())
                .diagnostico(request.getDiagnostico())
                .tratamiento(request.getTratamiento())
                .observaciones(request.getObservaciones())
                .medicamentos(request.getMedicamentos())
                .fechaConsulta(request.getFechaConsulta())
                .proximaConsulta(request.getProximaConsulta())
                .estado(HistorialMedico.EstadoHistorial.ACTIVO)
                .build();

        historial = historialMedicoRepository.save(historial);
        log.info("Historial médico creado con ID: {}", historial.getId());
        return HistorialMedicoResponse.fromEntity(historial);
    }

    @Override
    @Transactional(readOnly = true)
    public HistorialMedicoResponse obtenerPorId(Long id) {
        log.info("Buscando historial médico con ID: {}", id);
        HistorialMedico historial = historialMedicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Historial médico no encontrado con ID: " + id));
        return HistorialMedicoResponse.fromEntity(historial);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedicoResponse> obtenerPorPaciente(Long pacienteId) {
        log.info("Buscando historiales del paciente ID: {}", pacienteId);
        return historialMedicoRepository.findByPacienteIdOrderByFechaConsultaDesc(pacienteId)
                .stream()
                .map(HistorialMedicoResponse::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedicoResponse> obtenerPorMedico(Long medicoId) {
        log.info("Buscando historiales del médico ID: {}", medicoId);
        return historialMedicoRepository.findByMedicoIdOrderByFechaConsultaDesc(medicoId)
                .stream()
                .map(HistorialMedicoResponse::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedicoResponse> obtenerPorTurno(Long turnoId) {
        log.info("Buscando historial del turno ID: {}", turnoId);
        return historialMedicoRepository.findByTurnoId(turnoId)
                .stream()
                .map(HistorialMedicoResponse::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedicoResponse> obtenerPorPacienteYEstado(Long pacienteId, HistorialMedico.EstadoHistorial estado) {
        log.info("Buscando historiales del paciente ID: {} con estado: {}", pacienteId, estado);
        return historialMedicoRepository.findByPacienteIdAndEstado(pacienteId, estado)
                .stream()
                .map(HistorialMedicoResponse::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedicoResponse> obtenerPorPacienteYRangoFechas(Long pacienteId, LocalDate desde, LocalDate hasta) {
        log.info("Buscando historiales del paciente ID: {} entre {} y {}", pacienteId, desde, hasta);
        return historialMedicoRepository.findByPacienteIdAndFechaConsultaBetween(pacienteId, desde, hasta)
                .stream()
                .map(HistorialMedicoResponse::fromEntity)
                .toList();
    }

    @Override
    public HistorialMedicoResponse actualizar(Long id, HistorialMedicoRequest request) {
        log.info("Actualizando historial médico ID: {}", id);
        HistorialMedico historial = historialMedicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Historial médico no encontrado con ID: " + id));

        historial.setDiagnostico(request.getDiagnostico());
        historial.setTratamiento(request.getTratamiento());
        historial.setObservaciones(request.getObservaciones());
        historial.setMedicamentos(request.getMedicamentos());
        historial.setFechaConsulta(request.getFechaConsulta());
        historial.setProximaConsulta(request.getProximaConsulta());

        historial = historialMedicoRepository.save(historial);
        log.info("Historial médico ID: {} actualizado correctamente", id);
        return HistorialMedicoResponse.fromEntity(historial);
    }

    @Override
    public HistorialMedicoResponse cambiarEstado(Long id, HistorialMedico.EstadoHistorial nuevoEstado) {
        log.info("Cambiando estado del historial ID: {} a {}", id, nuevoEstado);
        HistorialMedico historial = historialMedicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Historial médico no encontrado con ID: " + id));

        historial.setEstado(nuevoEstado);
        historial = historialMedicoRepository.save(historial);
        return HistorialMedicoResponse.fromEntity(historial);
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando historial médico ID: {}", id);
        if (!historialMedicoRepository.existsById(id)) {
            throw new EntityNotFoundException("Historial médico no encontrado con ID: " + id);
        }
        historialMedicoRepository.deleteById(id);
    }

    // ─── Métodos privados de validación via Feign ────────────────────────────

    private void validarPaciente(Long pacienteId) {
        try {
            PacienteResponse paciente = pacienteClient.obtenerPorId(pacienteId);
            log.debug("Paciente validado: {} {}", paciente.getNombre(), paciente.getApellido());
        } catch (FeignException.NotFound e) {
            log.warn("Paciente no encontrado en ms-pacientes para ID: {}", pacienteId);
            throw new EntityNotFoundException("Paciente no encontrado con ID: " + pacienteId);
        } catch (FeignException e) {
            log.error("Error al comunicarse con ms-pacientes para ID: {}. Status: {}", pacienteId, e.status());
            throw new IllegalStateException("No se pudo verificar el paciente. Intente nuevamente.");
        }
    }

    private void validarMedico(Long medicoId) {
        try {
            MedicoResponse medico = medicoClient.obtenerPorId(medicoId);
            log.debug("Médico validado: {} {}", medico.getNombre(), medico.getApellido());
        } catch (FeignException.NotFound e) {
            log.warn("Médico no encontrado en ms-medicos para ID: {}", medicoId);
            throw new EntityNotFoundException("Médico no encontrado con ID: " + medicoId);
        } catch (FeignException e) {
            log.error("Error al comunicarse con ms-medicos para ID: {}. Status: {}", medicoId, e.status());
            throw new IllegalStateException("No se pudo verificar el médico. Intente nuevamente.");
        }
    }
}
