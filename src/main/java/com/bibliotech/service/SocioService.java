package com.bibliotech.service;

import com.bibliotech.model.Socio;
import com.bibliotech.repository.Repository;
import com.bibliotech.exception.BibliotecaException;
import java.util.Optional;

public class SocioService {
    private final Repository<Socio, Integer> socioRepository;

    public SocioService(Repository<Socio, Integer> socioRepository) {
        this.socioRepository = socioRepository;
    }

    public void registrarSocio(Socio socio) throws BibliotecaException {
        // Validación de DNI único
        if (socioRepository.buscarTodos().stream().anyMatch(s -> s.dni().equals(socio.dni()))) {
            throw new BibliotecaException("Ya existe un socio con el DNI: " + socio.dni());
        }

        // Validación de formato de email simple
        if (!socio.email().contains("@") || !socio.email().contains(".")) {
            throw new BibliotecaException("El formato del email es inválido: " + socio.email());
        }

        socioRepository.guardar(socio);
    }
}
