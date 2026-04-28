package com.bibliotech.model;

public record LibroFisico(
        String isbn,
        String titulo,
        String autor,
        int anio,
        Categoria categoria
) implements Recurso {
    @Override
    public String mostrarInfo() {
        return "[FÍSICO] " + titulo() + " | " + autor() + " | " + categoria() + " | ISBN: " + isbn();
    }
}
