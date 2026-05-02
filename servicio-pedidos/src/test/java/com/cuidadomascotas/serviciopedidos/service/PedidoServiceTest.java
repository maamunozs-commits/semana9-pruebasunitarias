package com.cuidadomascotas.serviciopedidos.service;

import com.cuidadomascotas.serviciopedidos.dto.EstadoPedidoDTO;
import com.cuidadomascotas.serviciopedidos.dto.PedidoDTO;
import com.cuidadomascotas.serviciopedidos.exception.RecursoNoEncontradoException;
import com.cuidadomascotas.serviciopedidos.model.EstadoPedido;
import com.cuidadomascotas.serviciopedidos.model.Pedido;
import com.cuidadomascotas.serviciopedidos.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private PedidoDTO pedidoDTO;

    @BeforeEach
    void setUp() {
        pedidoDTO = new PedidoDTO();
        pedidoDTO.setNombreProducto("Alimento Premium Perro 15kg");
        pedidoDTO.setCategoriaProducto("Alimento");
        pedidoDTO.setCantidad(2);
        pedidoDTO.setPrecioUnitario(new BigDecimal("35990"));
        pedidoDTO.setNombreCliente("Matias Munoz");
        pedidoDTO.setFechaPedido(LocalDate.of(2026, 5, 1));
        pedidoDTO.setEstado(EstadoPedido.PENDIENTE);
    }

    @Test
    void crearDebeGuardarPedidoValido() {
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido pedido = invocation.getArgument(0);
            pedido.setId(1L);
            return pedido;
        });

        Pedido creado = pedidoService.crear(pedidoDTO);

        assertEquals(1L, creado.getId());
        assertEquals("Alimento Premium Perro 15kg", creado.getNombreProducto());
        assertEquals(EstadoPedido.PENDIENTE, creado.getEstado());
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    void actualizarEstadoDebeCambiarEstadoCuandoExiste() {
        Pedido pedido = new Pedido(1L, "Arena Sanitaria Gato 10kg", "Higiene", 1,
                new BigDecimal("8990"), "Ana Torres", LocalDate.of(2026, 5, 2), EstadoPedido.PENDIENTE);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pedido actualizado = pedidoService.actualizarEstado(1L, new EstadoPedidoDTO(EstadoPedido.CONFIRMADO));

        assertEquals(EstadoPedido.CONFIRMADO, actualizado.getEstado());
        verify(pedidoRepository).save(pedido);
    }

    @Test
    void obtenerPorIdObligatorioDebeLanzarExcepcionSiNoExiste() {
        when(pedidoRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> pedidoService.obtenerPorIdObligatorio(10L));
    }
}
