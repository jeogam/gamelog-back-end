package br.com.ifba.gamelog.features.lista.service;

import br.com.ifba.gamelog.features.lista.model.ListaPersonalizada;
import java.util.List;

public interface ListaPersonalizadaIService {
    List<ListaPersonalizada> findAll();
    List<ListaPersonalizada> findByUsuario(Long usuarioId);
    ListaPersonalizada save(ListaPersonalizada lista);
    ListaPersonalizada update(ListaPersonalizada lista);
    void delete(Long id);
}