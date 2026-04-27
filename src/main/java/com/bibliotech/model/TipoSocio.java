package main.java.com.bibliotech.model;

public enum TipoSocio {
    ESTUDIANTE(3),
    DOCENTE(5);

    private final int limiteLibros;

    TipoSocio(int limiteLibros) {
        this.limiteLibros = limiteLibros;
    }

    public int getLimiteLibros() {
        return limiteLibros;
    }
}
