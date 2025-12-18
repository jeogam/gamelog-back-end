package br.com.ifba.gamelog.features.perfil.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

/**
 * Objeto de Transferência de Dados (DTO) para retorno dos dados de um Perfil.
 *
 * @param id           Identificador único do perfil.
 * @param nomeExibicao Nome de exibição do usuário.
 * @param biografia    Biografia do usuário.
 * @param avatarImagem URL/Referência do avatar.
 * @param usuarioId    ID do usuário vinculado a este perfil.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PerfilResponseDTO(
        @JsonProperty("id")
        Long id,

        @JsonProperty("nomeExibicao")
        String nomeExibicao,

        @JsonProperty("biografia")
        String biografia,

        @JsonProperty("avatarImagem")
        String avatarImagem,

        @JsonProperty("usuarioId")
        UUID usuarioId
) {}