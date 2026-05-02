package com.cuidadomascotas.serviciopedidos.dto;

import com.cuidadomascotas.serviciopedidos.model.EstadoPedido;
import jakarta.validation.constraints.NotNull;

public class EstadoPedidoDTO {

    @NotNull(message = "El estado es obligatorio")
    private EstadoPedido estado;

    public EstadoPedidoDTO() {
    }

    public EstadoPedidoDTO(EstadoPedido estado) {
        this.estado = estado;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }
}
