package br.com.ifba.gamelog.features.jogo.repository;

import br.com.ifba.gamelog.features.jogo.model.Jogo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Testes para JogoRepository")
class JogoRepositoryTest {

    @Autowired
    private IJogoRepository jogoRepository;

    @Test
    @DisplayName("existsByIdExterno retorna true quando ID já existe")
    void existsByIdExterno_WhenReturnTrue() {
        Jogo jogo = new Jogo();
        jogo.setTitulo("The Legend of Zelda");
        jogo.setIdExterno(12345L);
        jogo.setCapaUrl("http://img.com/zelda.jpg");

        jogoRepository.save(jogo);

        boolean existe = jogoRepository.existsByIdExterno(12345L);

        Assertions.assertThat(existe).isTrue();
    }
}