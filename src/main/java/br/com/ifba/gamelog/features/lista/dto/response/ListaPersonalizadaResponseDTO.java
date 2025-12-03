package br.com.ifba.gamelog.features.lista.dto.response;

import br.com.ifba.gamelog.features.jogo.dto.response.JogoResponseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ListaPersonalizadaResponseDTO(
        @JsonProperty("id")
        UUID id,

        @JsonProperty("nome")
        String nome,

        @JsonProperty("publica")
        boolean publica,

        @JsonProperty("usuarioId")
        UUID usuarioId,

        @JsonProperty("jogos")
        List<JogoResponseDTO> jogos
) {}