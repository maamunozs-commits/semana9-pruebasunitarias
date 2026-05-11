package com.cuidadomascotas.serviciocitas.service;

import com.cuidadomascotas.serviciocitas.dto.CitaDTO;
import com.cuidadomascotas.serviciocitas.dto.DisponibilidadDTO;
import com.cuidadomascotas.serviciocitas.dto.EstadoCitaDTO;
import com.cuidadomascotas.serviciocitas.model.Cita;
import com.cuidadomascotas.serviciocitas.model.EstadoCita;
import com.cuidadomascotas.serviciocitas.repository.CitaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CitaService - pruebas unitarias")
class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @InjectMocks
    private CitaService citaService;

    private CitaDTO citaDTO;

    @BeforeEach
    void setUp() {
        citaDTO = new CitaDTO();
        citaDTO.setNombrePaciente("Matias Munoz");
        citaDTO.setEmailPaciente("matias@correo.cl");
        citaDTO.setEspecialidad("Medicina General");
        citaDTO.setNombreMedico("Dra. Andrea Soto");
        citaDTO.setFechaCita(LocalDate.of(2026, 6, 10));
        citaDTO.setHoraCita(LocalTime.of(10, 30));
        citaDTO.setEstado(EstadoCita.PROGRAMADA);
    }

    @Test
    @DisplayName("Debe crear y guardar una cita valida")
    void crearDebeGuardarCitaValida() {
        when(citaRepository.save(any(Cita.class))).thenAnswer(invocation -> {
            Cita cita = invocation.getArgument(0);
            cita.setId(1L);
            return cita;
        });

        Cita creada = citaService.crear(citaDTO);

        assertAll(
                () -> assertEquals(1L, creada.getId()),
                () -> assertEquals("Matias Munoz", creada.getNombrePaciente()),
                () -> assertEquals(EstadoCita.PROGRAMADA, creada.getEstado())
        );
        verify(citaRepository).save(any(Cita.class));
    }

    @Test
    @DisplayName("Debe cancelar una cita programada")
    void cancelarDebeMarcarCitaComoCancelada() {
        Cita cita = new Cita(1L, "Matias Munoz", "matias@correo.cl", "Medicina General",
                "Dra. Andrea Soto", LocalDate.of(2026, 6, 10), LocalTime.of(10, 30),
                EstadoCita.PROGRAMADA);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any(Cita.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cita cancelada = citaService.cancelar(1L, new EstadoCitaDTO(EstadoCita.CANCELADA));

        assertEquals(EstadoCita.CANCELADA, cancelada.getEstado());
        verify(citaRepository).save(cita);
    }

    @Test
    @DisplayName("Debe calcular disponibilidad excluyendo horarios ocupados")
    void consultarDisponibilidadDebeExcluirHorariosOcupados() {
        LocalDate fecha = LocalDate.of(2026, 6, 10);
        Cita cita = new Cita(1L, "Paciente", "paciente@correo.cl", "Odontologia",
                "Dr. Carlos Reyes", fecha, LocalTime.of(9, 0), EstadoCita.CONFIRMADA);
        when(citaRepository.findByFechaCitaAndEstadoNot(fecha, EstadoCita.CANCELADA)).thenReturn(List.of(cita));

        DisponibilidadDTO disponibilidad = citaService.consultarDisponibilidad(fecha);

        assertFalse(disponibilidad.getHorariosDisponibles().contains(LocalTime.of(9, 0)));
        assertEquals(List.of(LocalTime.of(9, 0)), disponibilidad.getHorariosOcupados());
    }

    @Test
    @DisplayName("Debe rechazar cancelacion con un estado distinto de CANCELADA")
    void cancelarDebeRechazarEstadosDistintosDeCancelada() {
        EstadoCitaDTO dto = new EstadoCitaDTO(EstadoCita.CONFIRMADA);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> citaService.cancelar(1L, dto));

        assertEquals("El endpoint de cancelacion solo acepta el estado CANCELADA", exception.getMessage());
        verifyNoInteractions(citaRepository);
    }
}
