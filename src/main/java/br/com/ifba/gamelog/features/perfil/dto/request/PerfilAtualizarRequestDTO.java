package br.com.ifba.gamelog.features.perfil.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Objeto de Transferência de Dados (DTO) para atualização de um Perfil existente.
 *
 * @param id           ID único do perfil a ser atualizado.
 * @param nomeExibicao Novo nome de exibição.
 * @param biografia    Nova biografia.
 * @param avatarImagem Nova imagem de avatar.
 */
public record PerfilAtualizarRequestDTO(
        @NotNull(message = "O ID do perfil é obrigatório")
        Long id,

        @Size(max = 50, message = "O nome de exibição deve ter no máximo 50 caracteres")
        String nomeExibicao,

        @Size(max = 500, message = "A biografia deve ter no máximo 500 caracteres")
        String biografia,

        String avatarImagem
) {}