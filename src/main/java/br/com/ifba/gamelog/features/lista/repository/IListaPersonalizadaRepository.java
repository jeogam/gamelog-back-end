package br.com.ifba.gamelog.features.lista.repository;

import br.com.ifba.gamelog.features.lista.model.ListaPersonalizada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IListaPersonalizadaRepository extends JpaRepository<ListaPersonalizada, UUID> {

    /**
     * Busca todas as listas personalizadas criadas por um usuário específico.
     *
     * @param usuarioId UUID do usuário.
     * @return Lista de listas personalizadas.
     */
    List<ListaPersonalizada> findAllByUsuarioId(UUID usuarioId);
}