package ar.edu.daw1.tpintegrador.controller;

import ar.edu.daw1.tpintegrador.dto.EstadoRequest;
import ar.edu.daw1.tpintegrador.dto.PrestamoRequest;
import ar.edu.daw1.tpintegrador.entity.EstadoPrestamo;
import ar.edu.daw1.tpintegrador.entity.Prestamo;
import ar.edu.daw1.tpintegrador.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping
    public ResponseEntity<Prestamo> crear(
            @Valid @RequestBody PrestamoRequest request) {

        Prestamo creado = prestamoService.crear(
                request.libroId(),
                request.socioId(),
                request.fechaDevolucion()
        );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getId())
                .toUri();

        return ResponseEntity.created(location).body(creado);
    }

    @PatchMapping("/{id}/estado")
    public Prestamo cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoRequest request) {

        return prestamoService.cambiarEstado(id, request.estado());
    }

    @GetMapping
    public List<Prestamo> listar(
            @RequestParam(required = false) EstadoPrestamo estado) {

        return prestamoService.listarPorEstado(estado);
    }
}
