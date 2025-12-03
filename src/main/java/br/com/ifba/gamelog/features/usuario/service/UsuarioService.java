package br.com.ifba.gamelog.features.usuario.service;

import br.com.ifba.gamelog.features.usuario.dto.request.UsuarioAtualizarRequestDTO;
import br.com.ifba.gamelog.features.usuario.dto.request.UsuarioCriarRequestDTO;
import br.com.ifba.gamelog.features.usuario.dto.response.UsuarioResponseDTO;
import br.com.ifba.gamelog.features.usuario.model.Usuario;
import br.com.ifba.gamelog.features.usuario.repository.IUsuarioRepository;
import br.com.ifba.gamelog.infrastructure.exception.BusinessException;
import br.com.ifba.gamelog.infrastructure.exception.BusinessExceptionMessage;
import br.com.ifba.gamelog.infrastructure.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável pela lógica de negócio dos usuários.
 *
 * @author Seu Nome
 */
@Service
@RequiredArgsConstructor
public class UsuarioService implements IUsuarioService {

    private final IUsuarioRepository repository;
    private final ObjectMapperUtil objectMapperUtil;

    /**
     * Cria um novo usuário, verificando unicidade de email.
     *
     * @param dto Dados de entrada.
     * @return DTO do usuário salvo.
     * @throws BusinessException se o email já estiver em uso.
     */
    @Override
    @Transactional
    public UsuarioResponseDTO save(UsuarioCriarRequestDTO dto) {
        if (repository.existsByEmail(dto.email())) {
            throw new BusinessException(BusinessExceptionMessage.ATTRIBUTE_VALUE_ALREADY_EXISTS.getAttributeValueAlreadyExistsMessage("Email"));
        }

        Usuario entity = objectMapperUtil.map(dto, Usuario.class);
        Usuario savedEntity = repository.save(entity);

        return objectMapperUtil.map(savedEntity, UsuarioResponseDTO.class);
    }

    /**
     * Retorna todos os usuários.
     *
     * @return Lista de usuários.
     */
    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> findAll() {
        return objectMapperUtil.mapAll(repository.findAll(), UsuarioResponseDTO.class);
    }

    /**
     * Busca usuário por ID.
     *
     * @param id UUID do usuário.
     * @return DTO do usuário.
     * @throws BusinessException se não encontrado.
     */
    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(entity -> objectMapperUtil.map(entity, UsuarioResponseDTO.class))
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));
    }

    /**
     * Atualiza dados do usuário. Verifica conflito de email caso o email seja alterado.
     *
     * @param dto Dados atualizados.
     * @return Usuário atualizado.
     */
    @Override
    @Transactional
    public UsuarioResponseDTO update(UsuarioAtualizarRequestDTO dto) {
        Usuario usuarioExistente = repository.findById(dto.id())
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));

        // Verifica se trocou de email e se o novo já existe em OUTRO usuário
        if (!usuarioExistente.getEmail().equals(dto.email()) && repository.existsByEmail(dto.email())) {
            throw new BusinessException(BusinessExceptionMessage.ATTRIBUTE_VALUE_ALREADY_EXISTS.getAttributeValueAlreadyExistsMessage("Email"));
        }

        usuarioExistente.setNome(dto.nome());
        usuarioExistente.setEmail(dto.email());

        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuarioExistente.setSenha(dto.senha());
        }

        Usuario updatedEntity = repository.save(usuarioExistente);
        return objectMapperUtil.map(updatedEntity, UsuarioResponseDTO.class);
    }

    /**
     * Remove um usuário.
     *
     * @param id UUID a deletar.
     * @return O ID deletado.
     */
    @Override
    @Transactional
    public UUID delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage());
        }
        repository.deleteById(id);
        return id;
    }
}