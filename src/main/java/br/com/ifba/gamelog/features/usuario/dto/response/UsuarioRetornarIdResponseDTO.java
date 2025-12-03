package br.com.ifba.gamelog.features.usuario.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record UsuarioRetornarIdResponseDTO(
        @JsonProperty("id")
        UUID id
) {}