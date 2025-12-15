package br.com.ifba.gamelog.features.biblioteca.repository;

import br.com.ifba.gamelog.features.biblioteca.model.Biblioteca;
import br.com.ifba.gamelog.features.biblioteca.model.StatusJogo;
import br.com.ifba.gamelog.features.jogo.model.Jogo;
import br.com.ifba.gamelog.features.jogo.repository.IJogoRepository;
import br.com.ifba.gamelog.features.usuario.model.Usuario;
import br.com.ifba.gamelog.features.usuario.repository.IUsuarioRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Testes para BibliotecaRepository")
class BibliotecaRepositoryTest {

    @Autowired private IBibliotecaRepository bibliotecaRepository;
    @Autowired private IUsuarioRepository usuarioRepository;
    @Autowired private IJogoRepository jogoRepository;

    @Test
    @DisplayName("findAllByUsuarioId retorna lista de itens da biblioteca")
    void findAllByUsuarioId_WhenSuccessful() {
        Usuario user = usuarioRepository.save(new Usuario("User", "lib@test.com", "123", null, null, null));
        Jogo jogo = jogoRepository.save(new Jogo(888L, "Jogo Lib", "url", "desc", 2021, "PS5", "Ação", null, null, null));

        Biblioteca item = new Biblioteca(StatusJogo.JOGANDO, true, user, jogo);
        bibliotecaRepository.save(item);

        List<Biblioteca> lista = bibliotecaRepository.findAllByUsuarioId(user.getId());

        Assertions.assertThat(lista).isNotEmpty();
        Assertions.assertThat(lista).hasSize(1);
        Assertions.assertThat(lista.get(0).getJogo().getTitulo()).isEqualTo("Jogo Lib");
    }
}