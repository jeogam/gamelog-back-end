package br.com.ifba.gamelog.features.avaliacao.repository;

import br.com.ifba.gamelog.features.avaliacao.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    // Busca todas as avaliações de um jogo específico
    List<Avaliacao> findByJogoId(Long jogoId);
}