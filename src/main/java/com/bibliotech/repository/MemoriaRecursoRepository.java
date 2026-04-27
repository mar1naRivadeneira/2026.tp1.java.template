package com.bibliotech.repository;

import com.bibliotech.model.*;
import com.bibliotech.model.Categoria;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MemoriaRecursoRepository implements Repository<Recurso, String> {
    private final List<Recurso> recursos = new ArrayList<>();

    @Override
    public void guardar(Recurso recurso) {
        recursos.add(recurso);
    }

    @Override
    public Optional<Recurso> buscarPorId(String isbn) {
        return recursos.stream()
                .filter(r -> r.isbn().equals(isbn))
                .findFirst();
    }

    public List<Recurso> buscarPorTitulo(String titulo) {
        return recursos.stream()
                .filter(r -> r.titulo().toLowerCase().contains(titulo.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Recurso> buscarPorAutor(String autor) {
        return recursos.stream()
                .filter(r -> r.autor().toLowerCase().contains(autor.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Recurso> buscarPorCategoria(Categoria categoria) {
        return recursos.stream()
                .filter(r -> {
                    if (r instanceof LibroFisico libro) return libro.categoria().equals(categoria);
                    if (r instanceof Ebook ebook) return ebook.categoria().equals(categoria);
                    return false;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Recurso> buscarTodos() {
        return new ArrayList<>(recursos);
    }
}
