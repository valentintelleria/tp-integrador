package ar.edu.daw1.tpintegrador.repository;

import ar.edu.daw1.tpintegrador.entity.Socio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocioRepository extends JpaRepository<Socio, Long> {

    boolean existsByEmail(String email);

}
