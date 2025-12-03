package br.com.ifba.gamelog.features.avaliacao.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AvaliacaoResponseDTO(
        @JsonProperty("id")
        UUID id,

        @JsonProperty("nota")
        Integer nota,

        @JsonProperty("comentario")
        String comentario,

        @JsonProperty("usuarioId")
        UUID usuarioId,

        @JsonProperty("jogoId")
        UUID jogoId
) {}