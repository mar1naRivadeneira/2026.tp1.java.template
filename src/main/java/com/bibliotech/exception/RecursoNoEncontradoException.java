package com.bibliotech.exception;

public class RecursoNoEncontradoException extends BibliotecaException{
    public RecursoNoEncontradoException(String isbn) {
        super("El recurso con ID/ISBN '" + isbn + "' no se encuentra en el sistema.");
    }
}
