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
import feign.Request;
import feign.RequestTemplate;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistorialMedicoServiceTest {

    @Mock
    private HistorialMedicoRepository historialMedicoRepository;

    @Mock
    private PacienteClient pacienteClient;

    @Mock
    private MedicoClient medicoClient;

    @InjectMocks
    private HistorialMedicoServiceImpl historialMedicoService;

    private HistorialMedico historial;
    private HistorialMedicoRequest request;
    private PacienteResponse pacienteResponse;
    private MedicoResponse medicoResponse;

    @BeforeEach
    void setUp() {
        historial = HistorialMedico.builder()
                .id(1L)
                .pacienteId(10L)
                .medicoId(20L)
                .turnoId(30L)
                .diagnostico("Faringitis aguda")
                .tratamiento("Reposo e hidratación")
                .fechaConsulta(LocalDate.now())
                .estado(HistorialMedico.EstadoHistorial.ACTIVO)
                .build();

        request = new HistorialMedicoRequest();
        request.setPacienteId(10L);
        request.setMedicoId(20L);
        request.setTurnoId(30L);
        request.setDiagnostico("Faringitis aguda");
        request.setTratamiento("Reposo e hidratación");
        request.setFechaConsulta(LocalDate.now());

        pacienteResponse = new PacienteResponse();
        pacienteResponse.setId(10L);
        pacienteResponse.setNombre("Sofía");
        pacienteResponse.setApellido("Reyes");

        medicoResponse = new MedicoResponse();
        medicoResponse.setId(20L);
        medicoResponse.setNombre("Andrés");
        medicoResponse.setApellido("Muñoz");
    }



    @Test
    void testCrear_exitoso() {

        when(pacienteClient.obtenerPorId(10L)).thenReturn(pacienteResponse);
        when(medicoClient.obtenerPorId(20L)).thenReturn(medicoResponse);
        when(historialMedicoRepository.save(any(HistorialMedico.class))).thenReturn(historial);

        HistorialMedicoResponse resultado = historialMedicoService.crear(request);


        assertNotNull(resultado);
        assertEquals("Faringitis aguda", resultado.getDiagnostico());
        assertEquals(HistorialMedico.EstadoHistorial.ACTIVO, resultado.getEstado());
        verify(historialMedicoRepository, times(1)).save(any(HistorialMedico.class));
    }

    @Test
    void testCrear_pacienteNoExiste_lanzaEntityNotFoundException() {
        Request feignRequest = Request.create(Request.HttpMethod.GET, "/api/v1/pacientes/10",
                Collections.emptyMap(), null, new RequestTemplate());
        when(pacienteClient.obtenerPorId(10L))
                .thenThrow(new FeignException.NotFound("Not Found", feignRequest, null, null));


        assertThrows(EntityNotFoundException.class, () -> historialMedicoService.crear(request));
        verify(historialMedicoRepository, never()).save(any(HistorialMedico.class));
    }

    @Test
    void testCrear_medicoNoExiste_lanzaEntityNotFoundException() {
        Request feignRequest = Request.create(Request.HttpMethod.GET, "/api/v1/medicos/20",
                Collections.emptyMap(), null, new RequestTemplate());
        when(pacienteClient.obtenerPorId(10L)).thenReturn(pacienteResponse);
        when(medicoClient.obtenerPorId(20L))
                .thenThrow(new FeignException.NotFound("Not Found", feignRequest, null, null));

        assertThrows(EntityNotFoundException.class, () -> historialMedicoService.crear(request));
        verify(historialMedicoRepository, never()).save(any(HistorialMedico.class));
    }



    @Test
    void testObtenerPorId_exitoso() {
        when(historialMedicoRepository.findById(1L)).thenReturn(Optional.of(historial));

        HistorialMedicoResponse resultado = historialMedicoService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void testObtenerPorId_noExiste_lanzaExcepcion() {
        when(historialMedicoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> historialMedicoService.obtenerPorId(99L));
    }


    @Test
    void testObtenerPorPaciente() {
        when(historialMedicoRepository.findByPacienteIdOrderByFechaConsultaDesc(10L))
                .thenReturn(List.of(historial));

        List<HistorialMedicoResponse> resultado = historialMedicoService.obtenerPorPaciente(10L);


        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }



    @Test
    void testObtenerPorPacienteYEstado() {
        when(historialMedicoRepository.findByPacienteIdAndEstado(10L, HistorialMedico.EstadoHistorial.ACTIVO))
                .thenReturn(List.of(historial));

        List<HistorialMedicoResponse> resultado = historialMedicoService
                .obtenerPorPacienteYEstado(10L, HistorialMedico.EstadoHistorial.ACTIVO);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(HistorialMedico.EstadoHistorial.ACTIVO, resultado.get(0).getEstado());
    }



    @Test
    void testActualizar_exitoso() {
        request.setDiagnostico("Faringitis crónica");
        when(historialMedicoRepository.findById(1L)).thenReturn(Optional.of(historial));
        when(historialMedicoRepository.save(any(HistorialMedico.class))).thenAnswer(invocation -> invocation.getArgument(0));

        HistorialMedicoResponse resultado = historialMedicoService.actualizar(1L, request);

        assertNotNull(resultado);
        assertEquals("Faringitis crónica", resultado.getDiagnostico());
    }

    @Test
    void testActualizar_noExiste_lanzaExcepcion() {
        when(historialMedicoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> historialMedicoService.actualizar(99L, request));
        verify(historialMedicoRepository, never()).save(any(HistorialMedico.class));
    }



    @Test
    void testCambiarEstado_exitoso() {
        when(historialMedicoRepository.findById(1L)).thenReturn(Optional.of(historial));
        when(historialMedicoRepository.save(any(HistorialMedico.class))).thenAnswer(invocation -> invocation.getArgument(0));

        HistorialMedicoResponse resultado = historialMedicoService
                .cambiarEstado(1L, HistorialMedico.EstadoHistorial.ARCHIVADO);


        assertNotNull(resultado);
        assertEquals(HistorialMedico.EstadoHistorial.ARCHIVADO, resultado.getEstado());
    }



    @Test
    void testEliminar_exitoso() {
        when(historialMedicoRepository.existsById(1L)).thenReturn(true);

        historialMedicoService.eliminar(1L);

        verify(historialMedicoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminar_noExiste_lanzaExcepcion() {
        when(historialMedicoRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> historialMedicoService.eliminar(99L));
        verify(historialMedicoRepository, never()).deleteById(any());
    }
}
