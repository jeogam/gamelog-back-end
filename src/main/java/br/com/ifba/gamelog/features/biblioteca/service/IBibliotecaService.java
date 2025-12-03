package br.com.ifba.gamelog.features.biblioteca.service;

import br.com.ifba.gamelog.features.biblioteca.dto.request.BibliotecaAtualizarRequestDTO;
import br.com.ifba.gamelog.features.biblioteca.dto.request.BibliotecaCriarRequestDTO;
import br.com.ifba.gamelog.features.biblioteca.dto.response.BibliotecaResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IBibliotecaService {

    /**
     * Adiciona um jogo à biblioteca do usuário.
     * @param dto Dados para criação (Usuario, Jogo, Status, Favorito).
     * @return DTO do item criado.
     */
    BibliotecaResponseDTO save(BibliotecaCriarRequestDTO dto);

    /**
     * Lista todos os itens de biblioteca do sistema (Geral).
     * @return Lista de todos os jogos salvos por todos os usuários.
     */
    List<BibliotecaResponseDTO> findAll();

    /**
     * Busca um item específico da biblioteca pelo seu ID.
     * @param id Identificador único do item.
     * @return DTO do item encontrado.
     */
    BibliotecaResponseDTO findById(UUID id);

    /**
     * Lista todos os jogos que estão na biblioteca de um usuário específico.
     * @param usuarioId ID do usuário dono da biblioteca.
     * @return Lista de jogos desse usuário.
     */
    List<BibliotecaResponseDTO> findAllByUsuario(UUID usuarioId);

    /**
     * Atualiza o status ou favorito de um item da biblioteca.
     * @param dto Dados atualizados.
     * @return DTO atualizado.
     */
    BibliotecaResponseDTO update(BibliotecaAtualizarRequestDTO dto);

    /**
     * Remove um jogo da biblioteca.
     * @param id Identificador do item a ser removido.
     * @return O ID removido.
     */
    UUID delete(UUID id);
}