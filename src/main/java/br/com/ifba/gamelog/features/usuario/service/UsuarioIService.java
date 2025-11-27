package br.com.ifba.gamelog.features.usuario.service;

import br.com.ifba.gamelog.features.usuario.model.Usuario;
import java.util.List;

public interface UsuarioIService {

    List<Usuario> findAll();

    Usuario findById(Long id);

    Usuario save(Usuario usuario);

    Usuario update(Usuario usuario);

    void delete(Long id);
}