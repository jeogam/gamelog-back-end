package br.com.ifba.gamelog.features.avaliacao.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record AvaliacaoRetornarIdResponseDTO(
        @JsonProperty("id")
        UUID id
) {}