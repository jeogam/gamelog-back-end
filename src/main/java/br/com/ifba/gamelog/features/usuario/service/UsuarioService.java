package br.com.ifba.gamelog.features.usuario.service;

import br.com.ifba.gamelog.features.perfil.model.Perfil;
import br.com.ifba.gamelog.features.perfil.repository.IPerfilRepository;
import br.com.ifba.gamelog.features.usuario.dto.request.UsuarioAtualizarRequestDTO;
import br.com.ifba.gamelog.features.usuario.dto.request.UsuarioCriarRequestDTO;
import br.com.ifba.gamelog.features.usuario.dto.request.UsuarioPapelRequestDTO;
import br.com.ifba.gamelog.features.usuario.dto.response.UsuarioResponseDTO;
import br.com.ifba.gamelog.features.usuario.mapper.UsuarioMapper;
import br.com.ifba.gamelog.features.usuario.model.Usuario;
import br.com.ifba.gamelog.features.usuario.model.UsuarioRole;
import br.com.ifba.gamelog.features.usuario.repository.IUsuarioRepository;
import br.com.ifba.gamelog.infrastructure.exception.BusinessException;
import br.com.ifba.gamelog.infrastructure.exception.BusinessExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelas regras de negócio de Usuários.
 * Gerencia criação, atualização, exclusão e papéis de acesso.
 *
 * @author Seu Nome
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class UsuarioService implements IUsuarioService {

    private final IUsuarioRepository repository;
    private final IPerfilRepository perfilRepository;
    private final UsuarioMapper mapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Cria um novo usuário e automaticamente gera um perfil vazio associado.
     */
    @Override
    @Transactional
    public UsuarioResponseDTO save(UsuarioCriarRequestDTO dto) {
        // Validação de e-mail duplicado
        if (repository.existsByEmail(dto.email())) {
            throw new BusinessException(
                    BusinessExceptionMessage.ATTRIBUTE_VALUE_ALREADY_EXISTS.getAttributeValueAlreadyExistsMessage("Email")
            );
        }

        // Mapeamento e Criptografia
        Usuario entity = mapper.toEntity(dto);
        entity.setSenha(passwordEncoder.encode(dto.senha()));

        // Define Role padrão se nulo
        if (entity.getPapel() == null) {
            entity.setPapel(UsuarioRole.USUARIO);
        }

        // 1. Salva o Usuário primeiro para gerar o ID
        Usuario savedEntity = repository.save(entity);

        // 2. Criação automática do Perfil vazio (Regra de Negócio)
        Perfil novoPerfil = new Perfil();
        novoPerfil.setUsuario(savedEntity);
        novoPerfil.setNomeExibicao(savedEntity.getNome());
        novoPerfil.setBiografia("");

        perfilRepository.save(novoPerfil);

        return mapper.toResponse(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));
    }

    @Override
    @Transactional
    public UsuarioResponseDTO update(UsuarioAtualizarRequestDTO dto) {
        Usuario usuarioExistente = repository.findById(dto.id())
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));

        // Verifica duplicidade de email se o email foi alterado
        if (!usuarioExistente.getEmail().equals(dto.email()) && repository.existsByEmail(dto.email())) {
            throw new BusinessException(
                    BusinessExceptionMessage.ATTRIBUTE_VALUE_ALREADY_EXISTS.getAttributeValueAlreadyExistsMessage("Email")
            );
        }

        usuarioExistente.setNome(dto.nome());
        usuarioExistente.setEmail(dto.email());

        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuarioExistente.setSenha(passwordEncoder.encode(dto.senha()));
        }

        Usuario updatedEntity = repository.save(usuarioExistente);
        return mapper.toResponse(updatedEntity);
    }

    @Override
    @Transactional
    public UUID delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage());
        }
        repository.deleteById(id);
        return id;
    }

    @Override
    @Transactional
    public UsuarioResponseDTO updatePapel(UUID id, UsuarioPapelRequestDTO dto) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));

        usuario.setPapel(dto.papel());
        Usuario usuarioAtualizado = repository.save(usuario);

        return mapper.toResponse(usuarioAtualizado);
    }
}