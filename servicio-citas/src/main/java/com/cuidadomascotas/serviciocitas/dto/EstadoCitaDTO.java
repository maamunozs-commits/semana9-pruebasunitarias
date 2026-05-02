package com.cuidadomascotas.serviciocitas.dto;

import com.cuidadomascotas.serviciocitas.model.EstadoCita;
import jakarta.validation.constraints.NotNull;

public class EstadoCitaDTO {

    @NotNull(message = "El estado es obligatorio")
    private EstadoCita estado;

    public EstadoCitaDTO() {
    }

    public EstadoCitaDTO(EstadoCita estado) {
        this.estado = estado;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }
}
