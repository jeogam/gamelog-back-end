package br.com.ifba.gamelog.features.lista.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record ListaPersonalizadaRetornarIdResponseDTO(
        @JsonProperty("id")
        UUID id
) {}