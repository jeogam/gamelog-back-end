package br.com.ifba.gamelog.features.avaliacao.repository;

import br.com.ifba.gamelog.features.avaliacao.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IAvaliacaoRepository extends JpaRepository<Avaliacao, UUID> {

    /**
     * Verifica se já existe uma avaliação deste usuário para este jogo.
     */
    boolean existsByUsuarioIdAndJogoId(UUID usuarioId, UUID jogoId);

    /**
     * Busca avaliações só do usuário
     * @param usuarioId
     * @return
     */
    List<Avaliacao> findByUsuarioId(UUID usuarioId);

    /**
     * Busca todas as avaliações vinculadas a um jogo específico.
     *
     * @param jogoId UUID do jogo.
     * @return Lista de avaliações.
     */
    List<Avaliacao> findByJogoId(UUID jogoId);
}