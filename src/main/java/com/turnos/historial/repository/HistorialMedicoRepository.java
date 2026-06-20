package com.turnos.historial.repository;

import com.turnos.historial.model.HistorialMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HistorialMedicoRepository extends JpaRepository<HistorialMedico, Long> {

    List<HistorialMedico> findByPacienteIdOrderByFechaConsultaDesc(Long pacienteId);

    List<HistorialMedico> findByMedicoIdOrderByFechaConsultaDesc(Long medicoId);

    List<HistorialMedico> findByTurnoId(Long turnoId);

    List<HistorialMedico> findByPacienteIdAndEstado(Long pacienteId, HistorialMedico.EstadoHistorial estado);

    @Query("SELECT h FROM HistorialMedico h WHERE h.pacienteId = :pacienteId " +
           "AND h.fechaConsulta BETWEEN :desde AND :hasta ORDER BY h.fechaConsulta DESC")
    List<HistorialMedico> findByPacienteIdAndFechaConsultaBetween(
            @Param("pacienteId") Long pacienteId,
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta);

    @Query("SELECT h FROM HistorialMedico h WHERE h.medicoId = :medicoId " +
           "AND h.fechaConsulta = :fecha ORDER BY h.createdAt DESC")
    List<HistorialMedico> findByMedicoIdAndFechaConsulta(
            @Param("medicoId") Long medicoId,
            @Param("fecha") LocalDate fecha);

    boolean existsByPacienteIdAndTurnoId(Long pacienteId, Long turnoId);
}
