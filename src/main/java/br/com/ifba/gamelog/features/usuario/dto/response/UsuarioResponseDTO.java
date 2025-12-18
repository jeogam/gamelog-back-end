package br.com.ifba.gamelog.features.usuario.dto.response;

import br.com.ifba.gamelog.features.usuario.model.UsuarioRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UsuarioResponseDTO(

        @JsonProperty("id")
        UUID id,

        @JsonProperty("nome")
        String nome,

        @JsonProperty("email")
        String email,

        @JsonProperty("papel")
        UsuarioRole papel
) {}