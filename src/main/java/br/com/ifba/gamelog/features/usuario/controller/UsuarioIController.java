package br.com.ifba.gamelog.features.usuario.controller;

import br.com.ifba.gamelog.features.usuario.model.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UsuarioIController {

    ResponseEntity<List<Usuario>> findAll();

    ResponseEntity<Usuario> findById(@PathVariable Long id);

    ResponseEntity<Usuario> save(@RequestBody Usuario usuario);

    ResponseEntity<Usuario> update(@RequestBody Usuario usuario);

    ResponseEntity<Void> delete(@PathVariable Long id);
}