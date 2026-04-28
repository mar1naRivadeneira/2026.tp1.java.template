package com.bibliotech.exception;

public class PrestamoNoPermitidoException extends BibliotecaException {
    public PrestamoNoPermitidoException(String motivo) {
        super("No se puede realizar el préstamo: " + motivo);
    }
}
