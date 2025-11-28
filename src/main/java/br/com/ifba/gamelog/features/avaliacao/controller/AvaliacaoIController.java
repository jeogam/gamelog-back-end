package br.com.ifba.gamelog.features.avaliacao.controller;

import br.com.ifba.gamelog.features.avaliacao.model.Avaliacao;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface AvaliacaoIController {
    ResponseEntity<List<Avaliacao>> findAll();
    ResponseEntity<List<Avaliacao>> findByJogo(Long jogoId);
    ResponseEntity<Avaliacao> save(Avaliacao avaliacao);
    ResponseEntity<Void> update(Avaliacao avaliacao);
    ResponseEntity<Void> delete(Long id);
}