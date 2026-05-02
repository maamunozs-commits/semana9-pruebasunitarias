package com.cuidadomascotas.serviciopedidos.repository;

import com.cuidadomascotas.serviciopedidos.model.EstadoPedido;
import com.cuidadomascotas.serviciopedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByEstado(EstadoPedido estado);

    List<Pedido> findByCategoriaProductoIgnoreCase(String categoriaProducto);
}
