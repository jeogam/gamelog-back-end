package br.com.ifba.gamelog.features.avaliacao.repository;

import br.com.ifba.gamelog.features.avaliacao.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IAvaliacaoRepository extends JpaRepository<Avaliacao, UUID> {

    // Verifica se já existe uma avaliação deste usuário para este jogo
    boolean existsByUsuarioIdAndJogoId(UUID usuarioId, UUID jogoId);
}