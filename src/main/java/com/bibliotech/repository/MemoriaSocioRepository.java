package com.bibliotech.repository;

import com.bibliotech.model.Socio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemoriaSocioRepository implements Repository<Socio, Integer> {
    private final List<Socio> socios = new ArrayList<>();

    @Override
    public void guardar(Socio socio) {
        socios.add(socio);
    }

    @Override
    public Optional<Socio> buscarPorId(Integer id) {
        return socios.stream()
                .filter(s -> s.id() == id)
                .findFirst();
    }

    @Override
    public List<Socio> buscarTodos() {
        return new ArrayList<>(socios);
    }

}
