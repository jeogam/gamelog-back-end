package br.com.ifba.gamelog.features.biblioteca.repository;

import br.com.ifba.gamelog.features.biblioteca.model.Biblioteca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IBibliotecaRepository extends JpaRepository<Biblioteca, UUID> {

    /**
     * Verifica se o usuário já possui este jogo específico na biblioteca.
     */
    boolean existsByUsuarioIdAndJogoId(UUID usuarioId, UUID jogoId);

    /**
     * Busca todos os itens de biblioteca pertencentes a um usuário.
     *
     * @param usuarioId UUID do usuário.
     * @return Lista de itens da biblioteca.
     */
    List<Biblioteca> findAllByUsuarioId(UUID usuarioId);
}