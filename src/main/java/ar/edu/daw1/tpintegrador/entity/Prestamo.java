package ar.edu.daw1.tpintegrador.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "prestamos")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "libro_id")
    private Libro libro;

    @ManyToOne(optional = false)
    @JoinColumn(name = "socio_id")
    private Socio socio;

    private LocalDate fechaDevolucion;

    @Enumerated(EnumType.STRING)
    private EstadoPrestamo estado = EstadoPrestamo.ACTIVO;

    public Prestamo() {
    }

    public Prestamo(Libro libro, Socio socio, LocalDate fechaDevolucion) {
        this.libro = libro;
        this.socio = socio;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = EstadoPrestamo.ACTIVO;
    }

    public Long getId() {
        return id;
    }

    public Libro getLibro() {
        return libro;
    }

    public Socio getSocio() {
        return socio;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public void setEstado(EstadoPrestamo estado) {
        this.estado = estado;
    }
}