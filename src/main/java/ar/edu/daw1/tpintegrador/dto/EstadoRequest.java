package ar.edu.daw1.tpintegrador.dto;

import ar.edu.daw1.tpintegrador.entity.EstadoPrestamo;
import jakarta.validation.constraints.NotNull;

public record EstadoRequest(
        @NotNull(message = "El estado es obligatorio")
        EstadoPrestamo estado
) {
}
