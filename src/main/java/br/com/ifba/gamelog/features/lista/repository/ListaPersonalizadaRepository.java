package br.com.ifba.gamelog.features.lista.repository;

import br.com.ifba.gamelog.features.lista.model.ListaPersonalizada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ListaPersonalizadaRepository extends JpaRepository<ListaPersonalizada, Long> {
    // Busca listas de um usuário específico
    List<ListaPersonalizada> findByUsuarioId(Long usuarioId);
}