package br.com.ifba.gamelog.features.lista.repository;

import br.com.ifba.gamelog.features.lista.model.ListaPersonalizada;
import br.com.ifba.gamelog.features.usuario.model.Usuario;
import br.com.ifba.gamelog.features.usuario.repository.IUsuarioRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Testes para ListaPersonalizadaRepository")
class ListaPersonalizadaRepositoryTest {

    @Autowired private IListaPersonalizadaRepository listaRepository;
    @Autowired private IUsuarioRepository usuarioRepository;

    @Test
    @DisplayName("findAllByUsuarioId retorna listas do usuário")
    void findAllByUsuarioId_WhenSuccessful() {
        Usuario user = usuarioRepository.save(new Usuario("ListMaker", "list@test.com", "123", null, null, null));

        ListaPersonalizada lista = new ListaPersonalizada("Meus Favoritos", true, user, new ArrayList<>());
        listaRepository.save(lista);

        List<ListaPersonalizada> retorno = listaRepository.findAllByUsuarioId(user.getId());

        Assertions.assertThat(retorno).isNotEmpty();
        Assertions.assertThat(retorno.get(0).getNome()).isEqualTo("Meus Favoritos");
    }
}