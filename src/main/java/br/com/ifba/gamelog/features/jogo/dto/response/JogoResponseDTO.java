package br.com.ifba.gamelog.features.jogo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record JogoResponseDTO(
        @JsonProperty("id")
        UUID id,

        @JsonProperty("idExterno")
        Long idExterno,

        @JsonProperty("titulo")
        String titulo,

        @JsonProperty("capaUrl")
        String capaUrl,

        @JsonProperty("descricao")
        String descricao,

        @JsonProperty("anoLancamento")
        Integer anoLancamento,

        @JsonProperty("plataformas")
        String plataformas,

        @JsonProperty("genero")
        String genero
) {}