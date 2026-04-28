package com.bibliotech.service;

import com.bibliotech.model.Socio;
import com.bibliotech.repository.Repository;
import com.bibliotech.exception.*;

public class SocioService {
    private final Repository<Socio, Integer> socioRepository;

    public SocioService(Repository<Socio, Integer> socioRepository) {
        this.socioRepository = socioRepository;
    }

    public void registrarSocio(Socio socio) throws BibliotecaException {
        // Validación del DNI único
        boolean dniExiste = socioRepository.buscarTodos().stream()
                .anyMatch(s -> s.dni().equals(socio.dni()));
        if (dniExiste) {
            throw new BibliotecaException("Ya existe un socio con el DNI: " + socio.dni());
        }

        // Validación email
        if (!socio.email().contains("@") || !socio.email().contains(".")) {
            throw new EmailInvalidoException(socio.email());
        }

        socioRepository.guardar(socio);
    }
}
