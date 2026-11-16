package ar.edu.daw1.tpintegrador.repository;

import ar.edu.daw1.tpintegrador.entity.EstadoPrestamo;
import ar.edu.daw1.tpintegrador.entity.Libro;
import ar.edu.daw1.tpintegrador.entity.Prestamo;
import ar.edu.daw1.tpintegrador.entity.Socio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    boolean existsByLibroAndEstadoIn(
            Libro libro, List<EstadoPrestamo> estados);

    long countBySocioAndEstadoIn(
            Socio socio, List<EstadoPrestamo> estados);

    List<Prestamo> findBySocioId(Long socioId);

    List<Prestamo> findByEstado(EstadoPrestamo estado);

}
