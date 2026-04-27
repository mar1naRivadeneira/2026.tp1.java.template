package com.bibliotech.model;

import java.time.LocalDate;
import java.util.Optional;

public record Prestamo(
        String id,
        Socio socio,
        Recurso recurso,
        LocalDate fechaSalida,
        LocalDate fechaDevolucionPrevista,
        LocalDate fechaDevolucionReal
) {
    public Optional<LocalDate> getFechaDevolucionReal() {
        return Optional.ofNullable(fechaDevolucionReal);
    }
}
