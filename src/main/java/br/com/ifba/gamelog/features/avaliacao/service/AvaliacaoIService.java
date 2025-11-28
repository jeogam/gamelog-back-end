package br.com.ifba.gamelog.features.avaliacao.service;

import br.com.ifba.gamelog.features.avaliacao.model.Avaliacao;
import java.util.List;

public interface AvaliacaoIService {
    List<Avaliacao> findAll();
    List<Avaliacao> findByJogo(Long jogoId);
    Avaliacao save(Avaliacao avaliacao);
    Avaliacao update(Avaliacao avaliacao);
    void delete(Long id);
}