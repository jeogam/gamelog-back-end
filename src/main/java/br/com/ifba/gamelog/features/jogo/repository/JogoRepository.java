package br.com.ifba.gamelog.features.jogo.repository;

import br.com.ifba.gamelog.features.jogo.model.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JogoRepository extends JpaRepository<Jogo, Long> {
    // Busca customizada para atender ao requisito de pesquisa por nome
    List<Jogo> findByTituloContainingIgnoreCase(String titulo);
}