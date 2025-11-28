package br.com.ifba.gamelog.features.jogo.controller;

import br.com.ifba.gamelog.features.jogo.model.Jogo;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface JogoIController {
    ResponseEntity<List<Jogo>> findAll();
    ResponseEntity<List<Jogo>> findByTitulo(String titulo);
    ResponseEntity<Jogo> findById(Long id);
    ResponseEntity<Jogo> save(Jogo jogo);
    ResponseEntity<Void> update(Jogo jogo);
    ResponseEntity<Void> delete(Long id);
}