package com.bibliotech.exception;

public class PrestamoNoPermitidoException extends BibliotecaException {
    public PrestamoNoPermitidoException(String mensaje) {
        super("No se puede realizar el préstamo: " + mensaje);
    }
}
