package br.com.ifba.gamelog.features.jogo.repository;

import br.com.ifba.gamelog.features.jogo.model.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IJogoRepository extends JpaRepository<Jogo, UUID> {

    // Verifica unicidade do ID externo
    boolean existsByIdExterno(Long idExterno);

    // Busca o jogo pelo ID externo (Necessário para a idempotência)
    Optional<Jogo> findByIdExterno(Long idExterno);
}