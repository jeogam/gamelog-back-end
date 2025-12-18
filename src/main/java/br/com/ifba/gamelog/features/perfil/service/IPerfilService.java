package br.com.ifba.gamelog.features.perfil.service;

import br.com.ifba.gamelog.features.perfil.dto.request.PerfilAtualizarMeusDadosRequestDTO;
import br.com.ifba.gamelog.features.perfil.dto.request.PerfilAtualizarRequestDTO;
import br.com.ifba.gamelog.features.perfil.dto.request.PerfilCriarRequestDTO;
import br.com.ifba.gamelog.features.perfil.dto.response.PerfilResponseDTO;
import br.com.ifba.gamelog.infrastructure.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Interface que define o contrato para o serviço de gerenciamento de perfis de usuário.
 * <p>
 * Responsável por operações de criação, leitura, atualização e exclusão de perfis.
 * </p>
 */
public interface IPerfilService {

    /**
     * Cria um novo perfil para um usuário existente.
     * <p>
     * Valida se o usuário existe e se ele já não possui um perfil cadastrado (relação 1:1).
     * </p>
     *
     * @param dto Dados necessários para a criação do perfil.
     * @return DTO contendo os dados do perfil criado.
     * @throws BusinessException se o usuário não for encontrado ou já possuir perfil.
     */
    PerfilResponseDTO save(PerfilCriarRequestDTO dto);

    /**
     * Busca um perfil específico pelo seu ID (Long).
     *
     * @param id Identificador único do perfil.
     * @return DTO com os dados do perfil encontrado.
     * @throws BusinessException se o perfil não for encontrado.
     */
    PerfilResponseDTO findById(Long id);

    /**
     * Busca o perfil associado a um usuário específico através do ID do usuário (UUID).
     *
     * @param usuarioId Identificador único do usuário.
     * @return DTO com os dados do perfil encontrado.
     * @throws BusinessException se o perfil não for encontrado para o usuário informado.
     */
    PerfilResponseDTO findByUsuarioId(UUID usuarioId);

    /**
     * Recupera uma lista paginada de todos os perfis cadastrados.
     *
     * @param pageable Parâmetros de paginação (página, tamanho, ordenação).
     * @return Uma página de DTOs de perfis.
     */
    Page<PerfilResponseDTO> findAllPaged(Pageable pageable);

    /**
     * Atualiza os dados de um perfil existente.
     *
     * @param dto Dados atualizados do perfil (nome de exibição, bio, avatar).
     * @return DTO com os dados do perfil após a atualização.
     * @throws BusinessException se o perfil não for encontrado.
     */
    PerfilResponseDTO update(PerfilAtualizarRequestDTO dto);

    /**
     * Remove um perfil do sistema.
     *
     * @param id Identificador único do perfil a ser removido.
     * @throws BusinessException se o perfil não for encontrado.
     */
    void delete(Long id);

    /**
     * Atualiza os dados do perfil do usuário logado.
     *
     * @param usuarioId ID do usuário autenticado.
     * @param dto Dados para atualização.
     * @return Perfil atualizado.
     */
    PerfilResponseDTO updateByUsuarioId(UUID usuarioId, PerfilAtualizarMeusDadosRequestDTO dto);
}