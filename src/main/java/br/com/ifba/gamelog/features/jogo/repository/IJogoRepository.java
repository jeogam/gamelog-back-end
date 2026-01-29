package br.com.ifba.gamelog.features.jogo.repository;

import br.com.ifba.gamelog.features.jogo.model.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IJogoRepository extends JpaRepository<Jogo, UUID> {

    /**
     * Verifica se já existe um jogo cadastrado com este ID Externo (RAWG).
     */
    boolean existsByIdExterno(Long idExterno);

    /**
     * Busca um jogo pelo seu ID Externo.
     */
    Optional<Jogo> findByIdExterno(Long idExterno);
}