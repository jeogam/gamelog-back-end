package br.com.ifba.gamelog.features.jogo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record JogoRetornarIdResponseDTO(
        @JsonProperty("id")
        UUID id
) {}