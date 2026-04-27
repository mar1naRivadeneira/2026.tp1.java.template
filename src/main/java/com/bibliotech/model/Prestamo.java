package main.java.com.bibliotech.model;

import java.time.LocalDate;

public record Prestamo(
        String id,
        Socio socio,
        Recurso recurso,
        LocalDate fechaSalida,
        LocalDate fechaDevolucionPrevista,
        LocalDate fechaDevolucionReal
) {
}
