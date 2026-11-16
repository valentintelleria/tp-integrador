package ar.edu.daw1.tpintegrador.repository;

import ar.edu.daw1.tpintegrador.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    boolean existsByIsbn(String isbn);

}
