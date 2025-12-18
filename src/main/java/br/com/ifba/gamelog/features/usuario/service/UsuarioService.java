package br.com.ifba.gamelog.features.usuario.service;

import br.com.ifba.gamelog.features.perfil.model.Perfil; // 1. Import do Perfil
import br.com.ifba.gamelog.features.perfil.repository.IPerfilRepository; // 2. Import do Repository
import br.com.ifba.gamelog.features.usuario.dto.request.UsuarioAtualizarRequestDTO;
import br.com.ifba.gamelog.features.usuario.dto.request.UsuarioCriarRequestDTO;
import br.com.ifba.gamelog.features.usuario.dto.response.UsuarioResponseDTO;
import br.com.ifba.gamelog.features.usuario.model.Usuario;
import br.com.ifba.gamelog.features.usuario.model.UsuarioRole;
import br.com.ifba.gamelog.features.usuario.repository.IUsuarioRepository;
import br.com.ifba.gamelog.infrastructure.exception.BusinessException;
import br.com.ifba.gamelog.infrastructure.exception.BusinessExceptionMessage;
import br.com.ifba.gamelog.infrastructure.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService implements IUsuarioService {

    private final IUsuarioRepository repository;
    private final IPerfilRepository perfilRepository; // 3. Injeção do repositório de Perfil
    private final ObjectMapperUtil objectMapperUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UsuarioResponseDTO save(UsuarioCriarRequestDTO dto) {
        // Validação de e-mail duplicado
        if (repository.existsByEmail(dto.email())) {
            throw new BusinessException(BusinessExceptionMessage.ATTRIBUTE_VALUE_ALREADY_EXISTS.getAttributeValueAlreadyExistsMessage("Email"));
        }

        // Mapeamento e Criptografia
        Usuario entity = objectMapperUtil.map(dto, Usuario.class);
        entity.setSenha(passwordEncoder.encode(dto.senha()));

        // Define Role padrão se nulo
        if (entity.getPapel() == null) {
            entity.setPapel(UsuarioRole.USUARIO);
        }

        // 1. Salva o Usuário primeiro para gerar o ID
        Usuario savedEntity = repository.save(entity);

        // 2. Criação automática do Perfil vazio
        Perfil novoPerfil = new Perfil();
        novoPerfil.setUsuario(savedEntity);
        // Define o nome de exibição inicial como o nome do usuário (boa prática)
        novoPerfil.setNomeExibicao(savedEntity.getNome());
        novoPerfil.setBiografia(""); // String vazia para evitar null se necessário

        perfilRepository.save(novoPerfil);

        // Retorna o DTO do usuário
        return new UsuarioResponseDTO(
                savedEntity.getId(),
                savedEntity.getNome(),
                savedEntity.getEmail(),
                savedEntity.getPapel()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(entity -> new UsuarioResponseDTO(
                        entity.getId(),
                        entity.getNome(),
                        entity.getEmail(),
                        entity.getPapel()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable)
                .map(entity -> new UsuarioResponseDTO(
                        entity.getId(),
                        entity.getNome(),
                        entity.getEmail(),
                        entity.getPapel()
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(entity -> new UsuarioResponseDTO(
                        entity.getId(),
                        entity.getNome(),
                        entity.getEmail(),
                        entity.getPapel()
                ))
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));
    }

    @Override
    @Transactional
    public UsuarioResponseDTO update(UsuarioAtualizarRequestDTO dto) {
        Usuario usuarioExistente = repository.findById(dto.id())
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));

        if (!usuarioExistente.getEmail().equals(dto.email()) && repository.existsByEmail(dto.email())) {
            throw new BusinessException(BusinessExceptionMessage.ATTRIBUTE_VALUE_ALREADY_EXISTS.getAttributeValueAlreadyExistsMessage("Email"));
        }

        usuarioExistente.setNome(dto.nome());
        usuarioExistente.setEmail(dto.email());

        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuarioExistente.setSenha(passwordEncoder.encode(dto.senha()));
        }

        Usuario updatedEntity = repository.save(usuarioExistente);

        return new UsuarioResponseDTO(
                updatedEntity.getId(),
                updatedEntity.getNome(),
                updatedEntity.getEmail(),
                updatedEntity.getPapel()
        );
    }

    @Override
    @Transactional
    public UUID delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage());
        }
        // O banco deve deletar o perfil em cascata se configurado no BD,
        // caso contrário, seria necessário deletar o perfil aqui manualmente antes.
        repository.deleteById(id);
        return id;
    }

    @Override
    @Transactional
    public UsuarioResponseDTO updatePapel(UUID id, br.com.ifba.gamelog.features.usuario.dto.request.UsuarioPapelRequestDTO dto) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));

        usuario.setPapel(dto.papel());

        Usuario usuarioAtualizado = repository.save(usuario);

        return new UsuarioResponseDTO(
                usuarioAtualizado.getId(),
                usuarioAtualizado.getNome(),
                usuarioAtualizado.getEmail(),
                usuarioAtualizado.getPapel()
        );
    }
}