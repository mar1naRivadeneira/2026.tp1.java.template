package com.bibliotech.service;

import com.bibliotech.model.*;
import com.bibliotech.repository.*;
import com.bibliotech.exception.BibliotecaException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class PrestamoService {
    // Inyección de dependencias (Punto 4 de la consigna)
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
                .orElseThrow(() -> new BibliotecaException("Error: Socio no encontrado."));

        Recurso recurso = recursoRepo.buscarPorId(isbn)
                .orElseThrow(() -> new BibliotecaException("Error: Recurso no encontrado."));

        boolean yaPrestado = prestamoRepo.buscarTodos().stream()
                .anyMatch(p -> p.recurso().isbn().equals(isbn) && p.fechaDevolucionReal() == null);

        if (yaPrestado) {
            throw new BibliotecaException("El recurso con ISBN " + isbn + " ya se encuentra prestado.");
        }

        long prestamosActivos = prestamoRepo.buscarTodos().stream()
                .filter(p -> p.socio().id() == socioId && p.fechaDevolucionReal() == null)
                .count();

        if (prestamosActivos >= socio.tipoSocio().getLimiteLibros()) {
            throw new BibliotecaException("El socio " + socio.nombre() + " alcanzó su límite de "
                    + socio.tipoSocio().getLimiteLibros() + " préstamos.");
        }

        Prestamo nuevoPrestamo = new Prestamo(
                UUID.randomUUID().toString(),
                socio,
                recurso,
                LocalDate.now(),
                LocalDate.now().plusDays(7), // Plazo estándar de 7 días
                null
        );

        prestamoRepo.guardar(nuevoPrestamo);
    }

    public void devolverPrestamo(String isbn) throws BibliotecaException {
        // Buscar el préstamo activo para ese recurso
        Prestamo activo = prestamoRepo.buscarTodos().stream()
                .filter(p -> p.recurso().isbn().equals(isbn) && p.fechaDevolucionReal() == null)
                .findFirst()
                .orElseThrow(() -> new BibliotecaException("No hay un préstamo activo para el recurso solicitado."));

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
