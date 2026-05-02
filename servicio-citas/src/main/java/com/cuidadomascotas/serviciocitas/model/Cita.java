package com.cuidadomascotas.serviciocitas.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "CITAS")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOMBRE_PACIENTE", nullable = false, length = 100)
    private String nombrePaciente;

    @Column(name = "EMAIL_PACIENTE", nullable = false, length = 120)
    private String emailPaciente;

    @Column(name = "ESPECIALIDAD", nullable = false, length = 100)
    private String especialidad;

    @Column(name = "NOMBRE_MEDICO", nullable = false, length = 100)
    private String nombreMedico;

    @Column(name = "FECHA_CITA", nullable = false)
    private LocalDate fechaCita;

    @Convert(converter = LocalTimeStringConverter.class)
    @Column(name = "HORA_CITA", nullable = false, length = 5)
    private LocalTime horaCita;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false, length = 20)
    private EstadoCita estado;

    public Cita() {
    }

    public Cita(Long id, String nombrePaciente, String emailPaciente, String especialidad,
                String nombreMedico, LocalDate fechaCita, LocalTime horaCita, EstadoCita estado) {
        this.id = id;
        this.nombrePaciente = nombrePaciente;
        this.emailPaciente = emailPaciente;
        this.especialidad = especialidad;
        this.nombreMedico = nombreMedico;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getEmailPaciente() {
        return emailPaciente;
    }

    public void setEmailPaciente(String emailPaciente) {
        this.emailPaciente = emailPaciente;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getNombreMedico() {
        return nombreMedico;
    }

    public void setNombreMedico(String nombreMedico) {
        this.nombreMedico = nombreMedico;
    }

    public LocalDate getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(LocalDate fechaCita) {
        this.fechaCita = fechaCita;
    }

    public LocalTime getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(LocalTime horaCita) {
        this.horaCita = horaCita;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }
}
