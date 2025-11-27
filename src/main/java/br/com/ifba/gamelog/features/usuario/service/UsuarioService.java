package br.com.ifba.gamelog.features.usuario.service;

import br.com.ifba.gamelog.features.usuario.model.Usuario;
import br.com.ifba.gamelog.features.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor // O Lombok cria o construtor para injetar o Repository
public class UsuarioService implements UsuarioIService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
    }

    @Override
    public Usuario save(Usuario usuario) {
        // Regra de negócio: Verifica se o email já existe
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Email já cadastrado!");
        }

        // Define a data de criação automaticamente se estiver nula
        if (usuario.getCriadoEm() == null) {
            usuario.setCriadoEm(LocalDateTime.now());
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario update(Usuario usuario) {
        // Verifica se o usuário existe antes de atualizar
        if (usuario.getId() == null || !usuarioRepository.existsById(usuario.getId())) {
            throw new RuntimeException("Impossível atualizar: Usuário não existe.");
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Impossível deletar: Usuário não encontrado.");
        }
        usuarioRepository.deleteById(id);
    }
}