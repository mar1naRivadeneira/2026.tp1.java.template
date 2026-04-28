package com.bibliotech.service;

import com.bibliotech.exception.BibliotecaException;
import com.bibliotech.model.Categoria;
import com.bibliotech.model.Recurso;
import com.bibliotech.repository.Repository;
import java.util.List;

public class RecursoService {
    private final Repository<Recurso, String> recursoRepo;

    public RecursoService(Repository<Recurso, String> recursoRepo)  {
        this.recursoRepo = recursoRepo;
    }

    public void registrar(Recurso nuevoRecurso) throws BibliotecaException {
        boolean isbnDuplicado = recursoRepo.buscarTodos().stream()
                .anyMatch(r -> r.isbn().equalsIgnoreCase(nuevoRecurso.isbn()));

        if (isbnDuplicado) {
            throw new BibliotecaException("No se puede registrar: El ISBN " + nuevoRecurso.isbn() + " ya existe en el sistema.");
        }

        recursoRepo.guardar(nuevoRecurso);
    }

    public List<Recurso> buscarTodos() {
        return recursoRepo.buscarTodos();
    }

    public List<Recurso> buscarPorTitulo(String titulo) {
        return recursoRepo.buscarTodos().stream()
                .filter(r -> r.titulo().toLowerCase().contains(titulo.toLowerCase()))
                .toList();
    }

    public List<Recurso> buscarPorAutor(String autor) {
        return recursoRepo.buscarTodos().stream()
                .filter(r -> r.autor().toLowerCase().contains(autor.toLowerCase()))
                .toList();
    }

    public List<Recurso> buscarPorCategoria(Categoria categoria) {
        return recursoRepo.buscarTodos().stream()
                .filter(r -> r.categoria() == categoria)
                .toList();
    }
}
