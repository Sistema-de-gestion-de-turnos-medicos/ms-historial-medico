package com.turnos.historial.service;

import com.turnos.historial.datalizer.HistorialMedicoRequest;
import com.turnos.historial.datalizer.HistorialMedicoResponse;
import com.turnos.historial.model.HistorialMedico;

import java.time.LocalDate;
import java.util.List;

public interface HistorialMedicoService {

    HistorialMedicoResponse crear(HistorialMedicoRequest request);

    HistorialMedicoResponse obtenerPorId(Long id);

    List<HistorialMedicoResponse> obtenerPorPaciente(Long pacienteId);

    List<HistorialMedicoResponse> obtenerPorMedico(Long medicoId);

    List<HistorialMedicoResponse> obtenerPorTurno(Long turnoId);

    List<HistorialMedicoResponse> obtenerPorPacienteYEstado(Long pacienteId, HistorialMedico.EstadoHistorial estado);

    List<HistorialMedicoResponse> obtenerPorPacienteYRangoFechas(Long pacienteId, LocalDate desde, LocalDate hasta);

    HistorialMedicoResponse actualizar(Long id, HistorialMedicoRequest request);

    HistorialMedicoResponse cambiarEstado(Long id, HistorialMedico.EstadoHistorial nuevoEstado);

    void eliminar(Long id);
}
