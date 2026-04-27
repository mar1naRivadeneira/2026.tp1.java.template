package com.bibliotech.model;

public record Socio(
        int id,
        String nombre,
        String dni,
        String email,
        TipoSocio tipoSocio
) {
}
