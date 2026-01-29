package br.com.ifba.gamelog.features.perfil.repository;

import br.com.ifba.gamelog.features.perfil.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IPerfilRepository extends JpaRepository<Perfil, Long> {

    /**
     * Busca o perfil associado a um usuário específico.
     *
     * @param usuarioId UUID do usuário.
     * @return Optional contendo o perfil, se existir.
     */
    Optional<Perfil> findByUsuarioId(UUID usuarioId);

    /**
     * Verifica se existe um perfil associado ao usuário informado.
     *
     * @param usuarioId UUID do usuário.
     * @return true se existir, false caso contrário.
     */
    boolean existsByUsuarioId(UUID usuarioId);
}