package br.com.ifba.gamelog.features.usuario.service;

import br.com.ifba.gamelog.features.usuario.dto.request.UsuarioAtualizarRequestDTO;
import br.com.ifba.gamelog.features.usuario.dto.request.UsuarioCriarRequestDTO;
import br.com.ifba.gamelog.features.usuario.dto.response.UsuarioResponseDTO;
import br.com.ifba.gamelog.infrastructure.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Interface que define o contrato para o serviço de gerenciamento de usuários.
 */
public interface IUsuarioService {

    /**
     * Cria um novo usuário no sistema.
     * <p>
     * Valida se o email já existe e criptografa a senha antes de persistir.
     * Se o papel não for informado, define como USUARIO por padrão.
     * </p>
     *
     * @param dto Dados de entrada para criação do usuário.
     * @return DTO contendo os dados do usuário salvo (sem a senha, mas com o papel).
     * @throws BusinessException se o email já estiver cadastrado.
     */
    UsuarioResponseDTO save(UsuarioCriarRequestDTO dto);

    /**
     * Recupera a lista completa de usuários cadastrados (Não paginado).
     *
     * @return Lista de DTOs de resposta dos usuários.
     */
    List<UsuarioResponseDTO> findAll();

    /**
     * Lista os usuários com suporte a paginação.
     *
     * @param pageable Parâmetros de paginação (página, tamanho, ordenação).
     * @return Uma página de DTOs contendo os dados dos usuários.
     */
    Page<UsuarioResponseDTO> findAllPaged(Pageable pageable);

    /**
     * Busca um usuário específico pelo seu ID.
     *
     * @param id O identificador único (UUID) do usuário.
     * @return DTO com os dados do usuário encontrado.
     * @throws BusinessException se o usuário não for encontrado.
     */
    UsuarioResponseDTO findById(UUID id);

    /**
     * Atualiza os dados de um usuário existente.
     * <p>
     * Se o e-mail for alterado, verifica se o novo e-mail já está em uso.
     * Se uma nova senha for fornecida, ela será criptografada.
     * </p>
     *
     * @param dto Dados atualizados do usuário.
     * @return DTO com os dados do usuário após a atualização.
     * @throws BusinessException se o usuário não existir ou se o novo e-mail já estiver em uso.
     */
    UsuarioResponseDTO update(UsuarioAtualizarRequestDTO dto);

    /**
     * Remove um usuário do sistema.
     *
     * @param id O identificador único (UUID) do usuário a ser removido.
     * @return O UUID do usuário que foi deletado.
     * @throws BusinessException se o usuário não for encontrado.
     */
    UUID delete(UUID id);

    /**
     * Atualiza o papel de um usuário existente.
     * <p>
     * Ele atualiza o Papel (Role) de um usuário.
     * </p>
     *
     * @param dto Dados atualizados do usuário.
     * @return DTO com os dados do usuário após a atualização.
     * @throws BusinessException se o usuário não existir.
     */
    UsuarioResponseDTO updatePapel(java.util.UUID id, br.com.ifba.gamelog.features.usuario.dto.request.UsuarioPapelRequestDTO dto);
}