package br.com.ifba.gamelog.features.lista.controller;

import br.com.ifba.gamelog.features.lista.model.ListaPersonalizada;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface ListaPersonalizadaIController {
    ResponseEntity<List<ListaPersonalizada>> findAll();
    ResponseEntity<List<ListaPersonalizada>> findByUsuario(Long usuarioId);
    ResponseEntity<ListaPersonalizada> save(ListaPersonalizada lista);
    ResponseEntity<Void> update(ListaPersonalizada lista);
    ResponseEntity<Void> delete(Long id);
}