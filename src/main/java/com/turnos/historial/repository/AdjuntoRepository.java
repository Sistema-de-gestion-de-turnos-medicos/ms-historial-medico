package com.turnos.historial.repository;

import com.turnos.historial.model.Adjunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdjuntoRepository extends JpaRepository<Adjunto, Long> {

    List<Adjunto> findByHistorialMedicoId(Long historialId);

    void deleteByHistorialMedicoId(Long historialId);
}
