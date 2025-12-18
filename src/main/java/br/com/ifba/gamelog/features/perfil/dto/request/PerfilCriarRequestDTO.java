package br.com.ifba.gamelog.features.perfil.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/**
 * Objeto de Transferência de Dados (DTO) para criação de um novo Perfil.
 *
 * @param usuarioId    ID do usuário ao qual o perfil será associado.
 * @param nomeExibicao Nome público do usuário no perfil.
 * @param biografia    Texto descritivo sobre o usuário.
 * @param avatarImagem URL ou referência da imagem de avatar.
 */
public record PerfilCriarRequestDTO(
        @NotNull(message = "O ID do usuário é obrigatório")
        UUID usuarioId,

        @Size(max = 50, message = "O nome de exibição deve ter no máximo 50 caracteres")
        String nomeExibicao,

        @Size(max = 500, message = "A biografia deve ter no máximo 500 caracteres")
        String biografia,

        String avatarImagem
) {}