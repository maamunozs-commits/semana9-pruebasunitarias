package com.cuidadomascotas.serviciocitas.repository;

import com.cuidadomascotas.serviciocitas.model.Cita;
import com.cuidadomascotas.serviciocitas.model.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByEstado(EstadoCita estado);

    List<Cita> findByNombreMedicoContainingIgnoreCase(String nombreMedico);

    List<Cita> findByFechaCitaAndEstadoNot(LocalDate fechaCita, EstadoCita estado);
}
