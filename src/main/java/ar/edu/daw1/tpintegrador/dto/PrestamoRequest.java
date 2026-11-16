package ar.edu.daw1.tpintegrador.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;

import java.time.LocalDate;

public record PrestamoRequest(
        @NotNull(message = "El libro es obligatorio")
        Long libroId,

        @NotNull(message = "El socio es obligatorio")
        Long socioId,

        @NotNull(message = "La fecha de devolución es obligatoria")
        @FutureOrPresent(message = "La fecha de devolución no puede estar en el pasado")
        LocalDate fechaDevolucion
) {
}
