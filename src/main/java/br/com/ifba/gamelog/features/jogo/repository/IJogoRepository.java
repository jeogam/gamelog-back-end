package br.com.ifba.gamelog.features.jogo.repository;

import br.com.ifba.gamelog.features.jogo.model.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IJogoRepository extends JpaRepository<Jogo, UUID> {

    // Verifica unicidade do ID externo (ex: ID da API IGDB/Steam)
    boolean existsByIdExterno(Long idExterno);
}