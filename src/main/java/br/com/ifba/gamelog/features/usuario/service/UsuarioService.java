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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Serviço responsável pela lógica de negócio dos usuários.
 * Gerencia a criação, atualização, listagem e remoção de usuários (Gamers).
 *
 * @author Seu Nome
 */
@Service
@RequiredArgsConstructor
public class UsuarioService implements IUsuarioService {

    private final IUsuarioRepository repository;
    private final ObjectMapperUtil objectMapperUtil;

    // Injeção do PasswordEncoder para criptografar senhas antes de salvar
    private final PasswordEncoder passwordEncoder;

    /**
     * Cria um novo usuário no sistema.
     * <p>
     * Valida se o email já existe e criptografa a senha antes de persistir.
     * </p>
     *
     * @param dto Dados de entrada para criação do usuário.
     * @return DTO contendo os dados do usuário salvo (sem a senha).
     * @throws BusinessException se o email já estiver cadastrado.
     */
    @Override
    @Transactional
    public UsuarioResponseDTO save(UsuarioCriarRequestDTO dto) {
        if (repository.existsByEmail(dto.email())) {
            throw new BusinessException(BusinessExceptionMessage.ATTRIBUTE_VALUE_ALREADY_EXISTS.getAttributeValueAlreadyExistsMessage("Email"));
        }

        // Mapeamento de DTO para Entity (pode continuar usando ModelMapper, se UsuarioCriarRequestDTO não for um Record ou se ModelMapper suportar bem)
        Usuario entity = objectMapperUtil.map(dto, Usuario.class);

        // Criptografia da senha antes de salvar no banco
        entity.setSenha(passwordEncoder.encode(dto.senha()));

        Usuario savedEntity = repository.save(entity);

        // CORREÇÃO: Mapeamento manual para o Record UsuarioResponseDTO
        return new UsuarioResponseDTO(
                savedEntity.getId(),
                savedEntity.getNome(),
                savedEntity.getEmail()
        );
    }

    /**
     * Recupera a lista completa de usuários cadastrados (Não paginado).
     *
     * @return Lista de DTOs de resposta dos usuários.
     */
    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> findAll() {
        return repository.findAll().stream()
                // CORREÇÃO: Mapeamento manual para o Record
                .map(entity -> new UsuarioResponseDTO(
                        entity.getId(),
                        entity.getNome(),
                        entity.getEmail()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Recupera uma página de usuários cadastrados.
     *
     * @param pageable Configurações de paginação (página, tamanho, ordenação).
     * @return Uma página de DTOs de resposta dos usuários.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable)
                // CORREÇÃO: Mapeamento manual para o Record
                .map(entity -> new UsuarioResponseDTO(
                        entity.getId(),
                        entity.getNome(),
                        entity.getEmail()
                ));
    }


    /**
     * Busca um usuário específico pelo seu ID.
     *
     * @param id O identificador único (UUID) do usuário.
     * @return DTO com os dados do usuário encontrado.
     * @throws BusinessException se o usuário não for encontrado.
     */
    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO findById(UUID id) {
        return repository.findById(id)
                // CORREÇÃO: Mapeamento manual para o Record
                .map(entity -> new UsuarioResponseDTO(
                        entity.getId(),
                        entity.getNome(),
                        entity.getEmail()
                ))
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));
    }

    /**
     * Atualiza os dados de um usuário existente.
     * <p>
     * Se o e-mail for alterado, verifica se o novo e-mail já está em uso por outro usuário.
     * Se uma nova senha for fornecida, ela será criptografada antes de ser salva.
     * </p>
     *
     * @param dto Dados atualizados do usuário.
     * @return DTO com os dados do usuário após a atualização.
     * @throws BusinessException se o usuário não existir ou se o novo e-mail já estiver em uso.
     */
    @Override
    @Transactional
    public UsuarioResponseDTO update(UsuarioAtualizarRequestDTO dto) {
        Usuario usuarioExistente = repository.findById(dto.id())
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));

        // Validação de duplicidade de email na atualização
        if (!usuarioExistente.getEmail().equals(dto.email()) && repository.existsByEmail(dto.email())) {
            throw new BusinessException(BusinessExceptionMessage.ATTRIBUTE_VALUE_ALREADY_EXISTS.getAttributeValueAlreadyExistsMessage("Email"));
        }

        usuarioExistente.setNome(dto.nome());
        usuarioExistente.setEmail(dto.email());

        // Se o usuário enviou uma senha nova (não nula/vazia), criptografa e atualiza.
        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuarioExistente.setSenha(passwordEncoder.encode(dto.senha()));
        }

        Usuario updatedEntity = repository.save(usuarioExistente);

        // CORREÇÃO: Mapeamento manual para o Record
        return new UsuarioResponseDTO(
                updatedEntity.getId(),
                updatedEntity.getNome(),
                updatedEntity.getEmail()
        );
    }

    /**
     * Remove um usuário do sistema.
     *
     * @param id O identificador único (UUID) do usuário a ser removido.
     * @return O UUID do usuário que foi deletado.
     * @throws BusinessException se o usuário não for encontrado.
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