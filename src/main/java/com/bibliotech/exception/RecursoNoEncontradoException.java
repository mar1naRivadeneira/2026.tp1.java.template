package com.bibliotech.exception;

public class RecursoNoEncontradoException extends BibliotecaException{
    public RecursoNoEncontradoException(String identificador) {
        super("El recurso con ID/ISBN '" + identificador + "' no se encuentra en el sistema.");
    }
}
