package com.bibliotech.model;

public record Ebook(
        String isbn,
        String titulo,
        String autor,
        int anio,
        Categoria categoria,
        String formatoArchivo,
        double tamanioMB
) implements Recurso {
    @Override
    public String mostrarInfo() {
        return "[EBOOK] " + titulo() + " | " + autor() + " | " + categoria() +
                " | " + formatoArchivo() + " | " + tamanioMB() + "MB | ISBN: " + isbn();
    }
}
