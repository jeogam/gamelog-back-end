package br.com.ifba.gamelog.features.biblioteca.dto.response;

import br.com.ifba.gamelog.features.biblioteca.model.StatusJogo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BibliotecaResponseDTO(
        @JsonProperty("id")
        UUID id,

        @JsonProperty("status")
        StatusJogo status,

        @JsonProperty("favorito")
        boolean favorito,

        @JsonProperty("usuarioId")
        UUID usuarioId,

        @JsonProperty("jogoId")
        UUID jogoId,

        @JsonProperty("tituloJogo")
        String tituloJogo // Útil para o front exibir listas sem buscar o jogo dnv
) {}