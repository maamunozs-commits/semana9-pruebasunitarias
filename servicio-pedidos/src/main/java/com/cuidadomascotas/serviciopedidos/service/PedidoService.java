package com.cuidadomascotas.serviciopedidos.service;

import com.cuidadomascotas.serviciopedidos.dto.EstadoPedidoDTO;
import com.cuidadomascotas.serviciopedidos.dto.PedidoDTO;
import com.cuidadomascotas.serviciopedidos.exception.RecursoNoEncontradoException;
import com.cuidadomascotas.serviciopedidos.model.EstadoPedido;
import com.cuidadomascotas.serviciopedidos.model.Pedido;
import com.cuidadomascotas.serviciopedidos.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> obtenerTodos() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        log.debug("Se encontraron {} pedidos", pedidos.size());
        return pedidos;
    }

    public Optional<Pedido> obtenerPorId(Long id) {
        log.debug("Buscando pedido con id: {}", id);
        return pedidoRepository.findById(id);
    }

    public Pedido obtenerPorIdObligatorio(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un pedido con el ID " + id));
    }

    public List<Pedido> obtenerPorEstado(EstadoPedido estado) {
        log.debug("Buscando pedidos con estado: {}", estado);
        return pedidoRepository.findByEstado(estado);
    }

    public List<Pedido> obtenerPorCategoria(String categoria) {
        log.debug("Buscando pedidos con categoria: {}", categoria);
        return pedidoRepository.findByCategoriaProductoIgnoreCase(categoria);
    }

    public Pedido crear(PedidoDTO dto) {
        log.info("Creando pedido para cliente: {}", dto.getNombreCliente());
        Pedido pedido = new Pedido();
        pedido.setNombreProducto(dto.getNombreProducto());
        pedido.setCategoriaProducto(dto.getCategoriaProducto());
        pedido.setCantidad(dto.getCantidad());
        pedido.setPrecioUnitario(dto.getPrecioUnitario());
        pedido.setNombreCliente(dto.getNombreCliente());
        pedido.setFechaPedido(dto.getFechaPedido());
        pedido.setEstado(dto.getEstado());
        Pedido guardado = pedidoRepository.save(pedido);
        log.info("Pedido guardado con id: {}", guardado.getId());
        return guardado;
    }

    public Pedido actualizar(Long id, PedidoDTO dto) {
        log.info("Actualizando pedido con id: {}", id);
        Pedido pedido = obtenerPorIdObligatorio(id);
        pedido.setNombreProducto(dto.getNombreProducto());
        pedido.setCategoriaProducto(dto.getCategoriaProducto());
        pedido.setCantidad(dto.getCantidad());
        pedido.setPrecioUnitario(dto.getPrecioUnitario());
        pedido.setNombreCliente(dto.getNombreCliente());
        pedido.setFechaPedido(dto.getFechaPedido());
        pedido.setEstado(dto.getEstado());
        return pedidoRepository.save(pedido);
    }

    public Pedido actualizarEstado(Long id, EstadoPedidoDTO dto) {
        log.info("Actualizando estado del pedido {} a {}", id, dto.getEstado());
        Pedido pedido = obtenerPorIdObligatorio(id);
        pedido.setEstado(dto.getEstado());
        return pedidoRepository.save(pedido);
    }

    public void eliminar(Long id) {
        Pedido pedido = obtenerPorIdObligatorio(id);
        pedidoRepository.delete(pedido);
        log.info("Pedido con id {} eliminado", id);
    }
}

