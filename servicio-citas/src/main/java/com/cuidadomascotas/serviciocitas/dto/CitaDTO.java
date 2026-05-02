package com.cuidadomascotas.serviciocitas.dto;

import com.cuidadomascotas.serviciocitas.model.EstadoCita;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public class CitaDTO {

    @NotBlank(message = "El nombre del paciente es obligatorio")
    @Size(max = 100, message = "El nombre del paciente no puede superar 100 caracteres")
    private String nombrePaciente;

    @NotBlank(message = "El email del paciente es obligatorio")
    @Email(message = "El email del paciente debe tener un formato valido")
    @Size(max = 120, message = "El email del paciente no puede superar 120 caracteres")
    private String emailPaciente;

    @NotBlank(message = "La especialidad es obligatoria")
    @Size(max = 100, message = "La especialidad no puede superar 100 caracteres")
    private String especialidad;

    @NotBlank(message = "El nombre del medico es obligatorio")
    @Size(max = 100, message = "El nombre del medico no puede superar 100 caracteres")
    private String nombreMedico;

    @NotNull(message = "La fecha de la cita es obligatoria")
    @FutureOrPresent(message = "La fecha de la cita debe ser actual o futura")
    private LocalDate fechaCita;

    @NotNull(message = "La hora de la cita es obligatoria")
    private LocalTime horaCita;

    @NotNull(message = "El estado es obligatorio")
    private EstadoCita estado;

    public CitaDTO() {
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
