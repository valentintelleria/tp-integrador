package ar.edu.daw1.tpintegrador.service;

import ar.edu.daw1.tpintegrador.entity.*;
import ar.edu.daw1.tpintegrador.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;
    private final SocioRepository socioRepository;

    private static final List<EstadoPrestamo> ESTADOS_SIN_DEVOLVER = List.of(EstadoPrestamo.ACTIVO,
            EstadoPrestamo.VENCIDO);

    public PrestamoService(PrestamoRepository prestamoRepository,
            LibroRepository libroRepository,
            SocioRepository socioRepository) {
        this.prestamoRepository = prestamoRepository;
        this.libroRepository = libroRepository;
        this.socioRepository = socioRepository;
    }

    public Prestamo crear(Long libroId, Long socioId,
            LocalDate fechaDevolucion) {

        if (fechaDevolucion == null ||
                fechaDevolucion.isBefore(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha de devolución no puede estar en el pasado");
        }

        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Libro no encontrado"));

        Socio socio = socioRepository.findById(socioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Socio no encontrado"));

        if (prestamoRepository.existsByLibroAndEstadoIn(
                libro, ESTADOS_SIN_DEVOLVER)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El libro ya tiene un préstamo sin devolver");
        }

        long prestamosActivos = prestamoRepository
                .countBySocioAndEstadoIn(socio, ESTADOS_SIN_DEVOLVER);

        if (prestamosActivos >= 3) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El socio ya tiene tres préstamos sin devolver");
        }

        Prestamo prestamo = new Prestamo(
                libro, socio, fechaDevolucion);

        return prestamoRepository.save(prestamo);
    }

    public Prestamo cambiarEstado(Long id, EstadoPrestamo nuevoEstado) {

        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Préstamo no encontrado"));

        if (nuevoEstado == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El estado es obligatorio");
        }

        EstadoPrestamo estadoActual = prestamo.getEstado();

        if (estadoActual == EstadoPrestamo.DEVUELTO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Un préstamo devuelto no puede cambiar de estado");
        }

        if (nuevoEstado == EstadoPrestamo.ACTIVO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede volver al estado ACTIVO");
        }

        if (estadoActual == nuevoEstado) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El préstamo ya tiene ese estado");
        }

        prestamo.setEstado(nuevoEstado);

        return prestamoRepository.save(prestamo);
    }

    public List<Prestamo> listarPorSocio(Long socioId) {

        if (!socioRepository.existsById(socioId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Socio no encontrado");
        }

        return prestamoRepository.findBySocioId(socioId);
    }

    public List<Prestamo> listarPorEstado(EstadoPrestamo estado) {

        if (estado == null) {
            return prestamoRepository.findAll();
        }

        return prestamoRepository.findByEstado(estado);
    }
}
