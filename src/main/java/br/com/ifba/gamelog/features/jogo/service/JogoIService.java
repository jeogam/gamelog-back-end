package br.com.ifba.gamelog.features.jogo.service;

import br.com.ifba.gamelog.features.jogo.model.Jogo;
import java.util.List;

public interface JogoIService {
    List<Jogo> findAll();
    Jogo findById(Long id);
    List<Jogo> findByTitulo(String titulo);
    Jogo save(Jogo jogo);
    Jogo update(Jogo jogo);
    void delete(Long id);
}