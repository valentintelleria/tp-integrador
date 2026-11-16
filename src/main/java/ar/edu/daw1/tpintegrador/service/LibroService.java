package ar.edu.daw1.tpintegrador.service;

import ar.edu.daw1.tpintegrador.entity.EstadoPrestamo;
import ar.edu.daw1.tpintegrador.entity.Libro;
import ar.edu.daw1.tpintegrador.repository.LibroRepository;
import ar.edu.daw1.tpintegrador.repository.PrestamoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class LibroService {

    private final LibroRepository libroRepository;
    private final PrestamoRepository prestamoRepository;

    public LibroService(LibroRepository libroRepository,
                        PrestamoRepository prestamoRepository) {
        this.libroRepository = libroRepository;
        this.prestamoRepository = prestamoRepository;
    }

    public List<Libro> listar() {
        return libroRepository.findAll();
    }

    public Libro buscarPorId(Long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Libro no encontrado"));
    }

    public Libro crear(Libro libro) {
        if (libroRepository.existsByIsbn(libro.getIsbn())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El ISBN ya está registrado");
        }

        return libroRepository.save(libro);
    }

    public Libro modificar(Long id, Libro datos) {
        Libro libro = buscarPorId(id);

        if (!libro.getIsbn().equals(datos.getIsbn())
                && libroRepository.existsByIsbn(datos.getIsbn())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El ISBN ya está registrado");
        }

        libro.setTitulo(datos.getTitulo());
        libro.setAutor(datos.getAutor());
        libro.setIsbn(datos.getIsbn());

        return libroRepository.save(libro);
    }

    public void eliminar(Long id) {
        Libro libro = buscarPorId(id);

        boolean tienePrestamos = prestamoRepository
                .existsByLibroAndEstadoIn(
                        libro,
                        List.of(EstadoPrestamo.ACTIVO, EstadoPrestamo.VENCIDO)
                );

        if (tienePrestamos) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar un libro con un préstamo sin devolver");
        }

        libroRepository.delete(libro);
    }
}
