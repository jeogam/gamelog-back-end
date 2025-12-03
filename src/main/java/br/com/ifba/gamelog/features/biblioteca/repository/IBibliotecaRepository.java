package br.com.ifba.gamelog.features.biblioteca.repository;

import br.com.ifba.gamelog.features.biblioteca.model.Biblioteca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IBibliotecaRepository extends JpaRepository<Biblioteca, UUID> {

    // Verifica se o usuário já tem esse jogo na biblioteca
    boolean existsByUsuarioIdAndJogoId(UUID usuarioId, UUID jogoId);

    // Busca todos os itens de um usuário específico
    List<Biblioteca> findAllByUsuarioId(UUID usuarioId);
}