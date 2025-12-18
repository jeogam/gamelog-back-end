package br.com.ifba.gamelog.features.perfil.dto.request;

import jakarta.validation.constraints.Size;

/**
 * DTO para atualização dos dados do perfil do usuário logado.
 * Não requer ID, pois o usuário é identificado pelo Token.
 */
public record PerfilAtualizarMeusDadosRequestDTO(
        @Size(max = 50, message = "O nome de exibição deve ter no máximo 50 caracteres")
        String nomeExibicao,

        @Size(max = 500, message = "A biografia deve ter no máximo 500 caracteres")
        String biografia,

        String avatarImagem
) {}