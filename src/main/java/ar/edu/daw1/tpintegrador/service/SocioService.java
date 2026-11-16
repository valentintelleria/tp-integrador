package ar.edu.daw1.tpintegrador.service;

import ar.edu.daw1.tpintegrador.entity.Prestamo;
import ar.edu.daw1.tpintegrador.entity.Socio;
import ar.edu.daw1.tpintegrador.repository.PrestamoRepository;
import ar.edu.daw1.tpintegrador.repository.SocioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SocioService {

    private final SocioRepository socioRepository;
    private final PrestamoRepository prestamoRepository;

    public SocioService(SocioRepository socioRepository,
                        PrestamoRepository prestamoRepository) {
        this.socioRepository = socioRepository;
        this.prestamoRepository = prestamoRepository;
    }

    public Socio crear(Socio socio) {
        if (socioRepository.existsByEmail(socio.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El email ya está registrado");
        }

        return socioRepository.save(socio);
    }

    public Socio buscarPorId(Long id) {
        return socioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Socio no encontrado"));
    }

    public List<Prestamo> listarPrestamos(Long socioId) {
        buscarPorId(socioId);
        return prestamoRepository.findBySocioId(socioId);
    }
}
