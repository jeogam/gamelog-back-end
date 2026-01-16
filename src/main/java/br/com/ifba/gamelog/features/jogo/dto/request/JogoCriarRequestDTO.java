package br.com.ifba.gamelog.features.jogo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record JogoCriarRequestDTO(
        @NotNull(message = "O ID Externo é obrigatório")
        Long idExterno,

        String titulo,

        String capaUrl,

        String descricao,

        Integer anoLancamento,

        String plataformas,

        String genero
) {}