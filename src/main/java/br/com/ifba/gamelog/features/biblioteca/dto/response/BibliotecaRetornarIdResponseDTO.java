package br.com.ifba.gamelog.features.biblioteca.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record BibliotecaRetornarIdResponseDTO(
        @JsonProperty("id")
        UUID id
) {}