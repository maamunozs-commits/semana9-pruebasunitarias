package com.cuidadomascotas.serviciocitas.controller;

import com.cuidadomascotas.serviciocitas.dto.CitaDTO;
import com.cuidadomascotas.serviciocitas.dto.DisponibilidadDTO;
import com.cuidadomascotas.serviciocitas.dto.EstadoCitaDTO;
import com.cuidadomascotas.serviciocitas.model.Cita;
import com.cuidadomascotas.serviciocitas.model.EstadoCita;
import com.cuidadomascotas.serviciocitas.service.CitaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private static final Logger log = LoggerFactory.getLogger(CitaController.class);

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Cita>>> obtenerTodas() {
        log.info("GET /api/citas - Obteniendo todas las citas");
        List<EntityModel<Cita>> citas = citaService.obtenerTodas().stream()
                .map(this::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(citas)
                .add(linkTo(methodOn(CitaController.class).obtenerTodas()).withSelfRel())
                .add(linkTo(methodOn(CitaController.class).crear(null)).withRel("crear")));
    }

    @GetMapping("/disponibilidad")
    public ResponseEntity<EntityModel<DisponibilidadDTO>> consultarDisponibilidad(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        log.info("GET /api/citas/disponibilidad - Consultando disponibilidad para fecha {}", fecha);
        DisponibilidadDTO disponibilidad = citaService.consultarDisponibilidad(fecha);
        return ResponseEntity.ok(EntityModel.of(disponibilidad)
                .add(linkTo(methodOn(CitaController.class).consultarDisponibilidad(fecha)).withSelfRel())
                .add(linkTo(methodOn(CitaController.class).obtenerTodas()).withRel("citas"))
                .add(linkTo(methodOn(CitaController.class).crear(null)).withRel("crear")));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Cita>> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/citas/{} - Buscando cita por id", id);
        return ResponseEntity.ok(toModel(citaService.obtenerPorIdObligatorio(id)));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<CollectionModel<EntityModel<Cita>>> obtenerPorEstado(@PathVariable EstadoCita estado) {
        log.info("GET /api/citas/estado/{} - Filtrando citas por estado", estado);
        List<EntityModel<Cita>> citas = citaService.obtenerPorEstado(estado).stream()
                .map(this::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(citas)
                .add(linkTo(methodOn(CitaController.class).obtenerPorEstado(estado)).withSelfRel())
                .add(linkTo(methodOn(CitaController.class).obtenerTodas()).withRel("citas")));
    }

    @GetMapping("/medico/{nombreMedico}")
    public ResponseEntity<CollectionModel<EntityModel<Cita>>> obtenerPorMedico(@PathVariable String nombreMedico) {
        log.info("GET /api/citas/medico/{} - Filtrando citas por medico", nombreMedico);
        List<EntityModel<Cita>> citas = citaService.obtenerPorMedico(nombreMedico).stream()
                .map(this::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(citas)
                .add(linkTo(methodOn(CitaController.class).obtenerPorMedico(nombreMedico)).withSelfRel())
                .add(linkTo(methodOn(CitaController.class).obtenerTodas()).withRel("citas")));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Cita>> crear(@Valid @RequestBody CitaDTO citaDTO) {
        log.info("POST /api/citas - Creando nueva cita para paciente: {}", citaDTO.getNombrePaciente());
        Cita creada = citaService.crear(citaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(creada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Cita>> actualizar(@PathVariable Long id,
                                                        @Valid @RequestBody CitaDTO citaDTO) {
        log.info("PUT /api/citas/{} - Actualizando cita", id);
        return ResponseEntity.ok(toModel(citaService.actualizar(id, citaDTO)));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<EntityModel<Cita>> cancelar(@PathVariable Long id,
                                                      @Valid @RequestBody EstadoCitaDTO estadoDTO) {
        log.info("PATCH /api/citas/{}/cancelar - Cancelando cita", id);
        return ResponseEntity.ok(toModel(citaService.cancelar(id, estadoDTO)));
    }

    private EntityModel<Cita> toModel(Cita cita) {
        return EntityModel.of(cita)
                .add(linkTo(methodOn(CitaController.class).obtenerPorId(cita.getId())).withSelfRel())
                .add(linkTo(methodOn(CitaController.class).obtenerTodas()).withRel("citas"))
                .add(linkTo(methodOn(CitaController.class).crear(null)).withRel("crear"))
                .add(linkTo(methodOn(CitaController.class).actualizar(cita.getId(), null)).withRel("actualizar"))
                .add(linkTo(methodOn(CitaController.class).cancelar(cita.getId(), null)).withRel("cancelar"))
                .add(linkTo(methodOn(CitaController.class).consultarDisponibilidad(cita.getFechaCita())).withRel("disponibilidad"));
    }
}
