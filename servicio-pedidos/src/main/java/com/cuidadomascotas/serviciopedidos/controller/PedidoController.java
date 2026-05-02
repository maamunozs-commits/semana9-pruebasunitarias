package com.cuidadomascotas.serviciopedidos.controller;

import com.cuidadomascotas.serviciopedidos.dto.EstadoPedidoDTO;
import com.cuidadomascotas.serviciopedidos.dto.PedidoDTO;
import com.cuidadomascotas.serviciopedidos.model.EstadoPedido;
import com.cuidadomascotas.serviciopedidos.model.Pedido;
import com.cuidadomascotas.serviciopedidos.service.PedidoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private static final Logger log = LoggerFactory.getLogger(PedidoController.class);

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> obtenerTodos() {
        log.info("GET /api/pedidos - Obteniendo todos los pedidos");
        List<EntityModel<Pedido>> pedidos = pedidoService.obtenerTodos().stream()
                .map(this::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(pedidos)
                .add(linkTo(methodOn(PedidoController.class).obtenerTodos()).withSelfRel())
                .add(linkTo(methodOn(PedidoController.class).crear(null)).withRel("crear")));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Pedido>> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/pedidos/{} - Buscando pedido por id", id);
        return ResponseEntity.ok(toModel(pedidoService.obtenerPorIdObligatorio(id)));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> obtenerPorEstado(@PathVariable EstadoPedido estado) {
        log.info("GET /api/pedidos/estado/{} - Filtrando pedidos por estado", estado);
        List<EntityModel<Pedido>> pedidos = pedidoService.obtenerPorEstado(estado).stream()
                .map(this::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(pedidos)
                .add(linkTo(methodOn(PedidoController.class).obtenerPorEstado(estado)).withSelfRel())
                .add(linkTo(methodOn(PedidoController.class).obtenerTodos()).withRel("pedidos")));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> obtenerPorCategoria(@PathVariable String categoria) {
        log.info("GET /api/pedidos/categoria/{} - Filtrando pedidos por categoria", categoria);
        List<EntityModel<Pedido>> pedidos = pedidoService.obtenerPorCategoria(categoria).stream()
                .map(this::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(pedidos)
                .add(linkTo(methodOn(PedidoController.class).obtenerPorCategoria(categoria)).withSelfRel())
                .add(linkTo(methodOn(PedidoController.class).obtenerTodos()).withRel("pedidos")));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Pedido>> crear(@Valid @RequestBody PedidoDTO pedidoDTO) {
        log.info("POST /api/pedidos - Creando nuevo pedido para cliente: {}", pedidoDTO.getNombreCliente());
        Pedido creado = pedidoService.crear(pedidoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Pedido>> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody PedidoDTO pedidoDTO) {
        log.info("PUT /api/pedidos/{} - Actualizando pedido", id);
        return ResponseEntity.ok(toModel(pedidoService.actualizar(id, pedidoDTO)));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<EntityModel<Pedido>> actualizarEstado(@PathVariable Long id,
                                                                @Valid @RequestBody EstadoPedidoDTO estadoDTO) {
        log.info("PATCH /api/pedidos/{}/estado - Actualizando estado", id);
        return ResponseEntity.ok(toModel(pedidoService.actualizarEstado(id, estadoDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/pedidos/{} - Eliminando pedido", id);
        pedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<Pedido> toModel(Pedido pedido) {
        return EntityModel.of(pedido)
                .add(linkTo(methodOn(PedidoController.class).obtenerPorId(pedido.getId())).withSelfRel())
                .add(linkTo(methodOn(PedidoController.class).obtenerTodos()).withRel("pedidos"))
                .add(linkTo(methodOn(PedidoController.class).crear(null)).withRel("crear"))
                .add(linkTo(methodOn(PedidoController.class).actualizar(pedido.getId(), null)).withRel("actualizar"))
                .add(linkTo(methodOn(PedidoController.class).actualizarEstado(pedido.getId(), null)).withRel("actualizar-estado"));
    }
}
