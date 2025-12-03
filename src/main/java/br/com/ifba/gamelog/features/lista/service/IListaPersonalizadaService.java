package br.com.ifba.gamelog.features.lista.service;

import br.com.ifba.gamelog.features.lista.dto.request.ListaPersonalizadaAtualizarRequestDTO;
import br.com.ifba.gamelog.features.lista.dto.request.ListaPersonalizadaCriarRequestDTO;
import br.com.ifba.gamelog.features.lista.dto.response.ListaPersonalizadaResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IListaPersonalizadaService {

    /**
     * Cria uma nova lista personalizada.
     * @param dto Dados de criação.
     * @return A lista criada.
     */
    ListaPersonalizadaResponseDTO save(ListaPersonalizadaCriarRequestDTO dto);

    /**
     * Lista todas as listas do sistema.
     * @return Lista de DTOs.
     */
    List<ListaPersonalizadaResponseDTO> findAll();

    /**
     * Lista as listas de um usuário específico.
     * @param usuarioId ID do usuário.
     * @return Listas do usuário.
     */
    List<ListaPersonalizadaResponseDTO> findByUsuario(UUID usuarioId);

    /**
     * Busca uma lista pelo ID.
     * @param id ID da lista.
     * @return Detalhes da lista.
     */
    ListaPersonalizadaResponseDTO findById(UUID id);

    /**
     * Atualiza dados da lista (nome, pública, jogos).
     * @param dto Dados atualizados.
     * @return Lista atualizada.
     */
    ListaPersonalizadaResponseDTO update(ListaPersonalizadaAtualizarRequestDTO dto);

    /**
     * Remove uma lista do sistema.
     * @param id ID da lista.
     * @return ID removido.
     */
    UUID delete(UUID id);
}