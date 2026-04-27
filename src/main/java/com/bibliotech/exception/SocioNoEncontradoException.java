package main.java.com.bibliotech.exception;

public class SocioNoEncontradoException extends BibliotecaException {
    public SocioNoEncontradoException(int id) {
        super("El socio con ID " + id + " no ha sido encontrado en el registro.");
    }
}
