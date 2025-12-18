package br.com.ifba.gamelog.features.perfil.repository;

import br.com.ifba.gamelog.features.perfil.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IPerfilRepository extends JpaRepository<Perfil, Long> {

    // Busca o perfil pelo ID do Usuário (que é UUID)
    Optional<Perfil> findByUsuarioId(UUID usuarioId);

    // Verifica existência pelo ID do Usuário
    boolean existsByUsuarioId(UUID usuarioId);
}