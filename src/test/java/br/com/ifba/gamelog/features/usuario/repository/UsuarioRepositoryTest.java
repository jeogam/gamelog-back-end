package br.com.ifba.gamelog.features.usuario.repository;

import br.com.ifba.gamelog.features.usuario.model.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Testes para UsuarioRepository")
class UsuarioRepositoryTest {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Test
    @DisplayName("findByEmail retorna usuário quando bem sucedido")
    void findByEmail_WhenSuccessful() {
        // Cenário
        Usuario usuario = criarUsuario();
        usuarioRepository.save(usuario);

        // Ação
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail("gamer@ifba.edu.br");

        // Verificação
        Assertions.assertThat(usuarioEncontrado).isPresent();
        Assertions.assertThat(usuarioEncontrado.get().getEmail()).isEqualTo("gamer@ifba.edu.br");
        Assertions.assertThat(usuarioEncontrado.get().getNome()).isEqualTo("Gamer Teste");
    }

    @Test
    @DisplayName("existsByEmail retorna true quando email já existe")
    void existsByEmail_WhenReturnTrue() {
        Usuario usuario = criarUsuario();
        usuarioRepository.save(usuario);

        boolean existe = usuarioRepository.existsByEmail("gamer@ifba.edu.br");

        Assertions.assertThat(existe).isTrue();
    }

    private Usuario criarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome("Gamer Teste");
        usuario.setEmail("gamer@ifba.edu.br");
        usuario.setSenha("123456");
        return usuario;
    }
}