package br.com.ifba.gamelog.features.usuario.repository;

import br.com.ifba.gamelog.features.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, UUID> {

    // Verifica se o email já existe para evitar duplicidade
    boolean existsByEmail(String email);
}