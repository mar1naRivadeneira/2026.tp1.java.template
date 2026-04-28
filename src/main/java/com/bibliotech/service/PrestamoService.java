package com.bibliotech.service;

import com.bibliotech.model.*;
import com.bibliotech.repository.*;
import com.bibliotech.exception.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class PrestamoService {
    private final Repository<Recurso, String> recursoRepo;
    private final Repository<Socio, Integer> socioRepo;
    private final Repository<Prestamo, String> prestamoRepo;

    public PrestamoService(Repository<Recurso, String> recursoRepo,
                           Repository<Socio, Integer> socioRepo,
                           Repository<Prestamo, String> prestamoRepo) {
        this.recursoRepo = recursoRepo;
        this.socioRepo = socioRepo;
        this.prestamoRepo = prestamoRepo;
    }

    public void realizarPrestamo(String isbn, int socioId) throws BibliotecaException {
        Socio socio = socioRepo.buscarPorId(socioId)
                .orElseThrow(() -> new SocioNoEncontradoException(socioId));

        Recurso recurso = recursoRepo.buscarPorId(isbn)
                .orElseThrow(() -> new RecursoNoEncontradoException(isbn));

        boolean yaPrestado = prestamoRepo.buscarTodos().stream()
                .anyMatch(p -> p.recurso().isbn().equals(isbn) && p.getFechaDevolucionReal().isEmpty());

        if (yaPrestado) {
            throw new PrestamoNoPermitidoException("El recurso ya se encuentra prestado.");
        }

        long prestamosActivos = prestamoRepo.buscarTodos().stream()
                .filter(p -> p.socio().id() == socioId && p.getFechaDevolucionReal().isEmpty())
                .count();

        if (prestamosActivos >= socio.tipoSocio().getLimiteLibros()) {
            throw new PrestamoNoPermitidoException("El socio " + socio.nombre() + " alcanzó su límite de "
                    + socio.tipoSocio().getLimiteLibros() + " préstamos.");
        }

        Prestamo nuevoPrestamo = new Prestamo(
                UUID.randomUUID().toString(),
                socio,
                recurso,
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                null
        );

        prestamoRepo.guardar(nuevoPrestamo);
    }

    public void devolverPrestamo(String isbn) throws BibliotecaException {

        Prestamo activo = prestamoRepo.buscarTodos().stream()
                .filter(p -> p.recurso().isbn().equals(isbn) && p.getFechaDevolucionReal().isEmpty())
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("No hay un préstamo activo para el ISBN: " + isbn));

        LocalDate hoy = LocalDate.now();

        if (hoy.isAfter(activo.fechaDevolucionPrevista())) {
            long diasRetraso = ChronoUnit.DAYS.between(activo.fechaDevolucionPrevista(), hoy);
            System.out.println("SISTEMA: Devolución registrada con " + diasRetraso + " días de retraso.");
        } else {
            System.out.println("SISTEMA: Devolución realizada en tiempo y forma.");
        }

        Prestamo devolucion = new Prestamo(
                activo.id(),
                activo.socio(),
                activo.recurso(),
                activo.fechaSalida(),
                activo.fechaDevolucionPrevista(),
                hoy
        );

        prestamoRepo.guardar(devolucion);
    }
}
