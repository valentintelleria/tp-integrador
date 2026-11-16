package ar.edu.daw1.tpintegrador.controller;

import ar.edu.daw1.tpintegrador.entity.Socio;
import ar.edu.daw1.tpintegrador.service.SocioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/socios")
public class SocioController {

    private final SocioService socioService;

    public SocioController(SocioService socioService) {
        this.socioService = socioService;
    }

    @PostMapping
    public ResponseEntity<Socio> crear(@Valid @RequestBody Socio socio) {
        Socio creado = socioService.crear(socio);

        URI location = URI.create("/api/socios/" + creado.getId());

        return ResponseEntity.created(location).body(creado);
    }

    @GetMapping("/{id}/prestamos")
    public ResponseEntity<?> listarPrestamos(@PathVariable Long id) {
        return ResponseEntity.ok(
                socioService.listarPrestamos(id)
        );
    }
}
