package br.com.ifba.gamelog.features.avaliacao.repository;

import br.com.ifba.gamelog.features.avaliacao.model.Avaliacao;
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

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Testes para AvaliacaoRepository")
class AvaliacaoRepositoryTest {

    @Autowired private IAvaliacaoRepository avaliacaoRepository;
    @Autowired private IUsuarioRepository usuarioRepository;
    @Autowired private IJogoRepository jogoRepository;

    @Test
    @DisplayName("existsByUsuarioIdAndJogoId retorna true quando avaliação existe")
    void existsByUsuarioIdAndJogoId_WhenReturnTrue() {
        // 1. Criar e salvar dependências
        Usuario user = new Usuario("User", "user@test.com", "123", null, null, null);
        usuarioRepository.save(user);

        Jogo jogo = new Jogo(999L, "Jogo Teste", "url", "desc", 2022, "PC", "RPG", null, null, null);
        jogoRepository.save(jogo);

        // 2. Criar e salvar avaliação
        Avaliacao avaliacao = new Avaliacao(5, "Muito bom!", user, jogo);
        avaliacaoRepository.save(avaliacao);

        // 3. Testar
        boolean existe = avaliacaoRepository.existsByUsuarioIdAndJogoId(user.getId(), jogo.getId());

        Assertions.assertThat(existe).isTrue();
    }
}