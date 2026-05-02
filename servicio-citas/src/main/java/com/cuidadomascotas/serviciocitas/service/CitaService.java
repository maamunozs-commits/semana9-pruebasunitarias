package com.cuidadomascotas.serviciocitas.service;

import com.cuidadomascotas.serviciocitas.dto.CitaDTO;
import com.cuidadomascotas.serviciocitas.dto.DisponibilidadDTO;
import com.cuidadomascotas.serviciocitas.dto.EstadoCitaDTO;
import com.cuidadomascotas.serviciocitas.exception.RecursoNoEncontradoException;
import com.cuidadomascotas.serviciocitas.model.Cita;
import com.cuidadomascotas.serviciocitas.model.EstadoCita;
import com.cuidadomascotas.serviciocitas.repository.CitaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

@Service
public class CitaService {

    private static final Logger log = LoggerFactory.getLogger(CitaService.class);
    private static final LocalTime PRIMER_HORARIO = LocalTime.of(9, 0);
    private static final LocalTime ULTIMO_HORARIO = LocalTime.of(17, 0);

    private final CitaRepository citaRepository;

    public CitaService(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    public List<Cita> obtenerTodas() {
        List<Cita> citas = citaRepository.findAll();
        log.debug("Se encontraron {} citas", citas.size());
        return citas;
    }

    public Cita obtenerPorIdObligatorio(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una cita con el ID " + id));
    }

    public List<Cita> obtenerPorEstado(EstadoCita estado) {
        log.debug("Buscando citas con estado: {}", estado);
        return citaRepository.findByEstado(estado);
    }

    public List<Cita> obtenerPorMedico(String nombreMedico) {
        log.debug("Buscando citas del medico: {}", nombreMedico);
        return citaRepository.findByNombreMedicoContainingIgnoreCase(nombreMedico);
    }

    public Cita crear(CitaDTO dto) {
        log.info("Creando cita para paciente: {}", dto.getNombrePaciente());
        Cita cita = new Cita();
        aplicarDatos(cita, dto);
        Cita guardada = citaRepository.save(cita);
        log.info("Cita guardada con id: {}", guardada.getId());
        return guardada;
    }

    public Cita actualizar(Long id, CitaDTO dto) {
        log.info("Actualizando cita con id: {}", id);
        Cita cita = obtenerPorIdObligatorio(id);
        aplicarDatos(cita, dto);
        return citaRepository.save(cita);
    }

    public Cita cancelar(Long id, EstadoCitaDTO dto) {
        log.info("Cancelando cita con id: {}", id);
        if (dto.getEstado() != EstadoCita.CANCELADA) {
            throw new IllegalArgumentException("El endpoint de cancelacion solo acepta el estado CANCELADA");
        }
        Cita cita = obtenerPorIdObligatorio(id);
        cita.setEstado(EstadoCita.CANCELADA);
        return citaRepository.save(cita);
    }

    public DisponibilidadDTO consultarDisponibilidad(LocalDate fecha) {
        List<LocalTime> ocupados = citaRepository.findByFechaCitaAndEstadoNot(fecha, EstadoCita.CANCELADA)
                .stream()
                .map(Cita::getHoraCita)
                .distinct()
                .sorted()
                .toList();

        List<LocalTime> disponibles = horariosDelDia().stream()
                .filter(horario -> !ocupados.contains(horario))
                .toList();

        return new DisponibilidadDTO(fecha, disponibles, ocupados);
    }

    private void aplicarDatos(Cita cita, CitaDTO dto) {
        cita.setNombrePaciente(dto.getNombrePaciente());
        cita.setEmailPaciente(dto.getEmailPaciente());
        cita.setEspecialidad(dto.getEspecialidad());
        cita.setNombreMedico(dto.getNombreMedico());
        cita.setFechaCita(dto.getFechaCita());
        cita.setHoraCita(dto.getHoraCita());
        cita.setEstado(dto.getEstado());
    }

    private List<LocalTime> horariosDelDia() {
        return Stream.iterate(PRIMER_HORARIO,
                        horario -> !horario.isAfter(ULTIMO_HORARIO),
                        horario -> horario.plusMinutes(30))
                .toList();
    }
}
