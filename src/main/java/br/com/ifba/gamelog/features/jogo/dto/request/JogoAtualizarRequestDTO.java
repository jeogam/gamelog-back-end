package br.com.ifba.gamelog.features.jogo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record JogoAtualizarRequestDTO(
        @NotNull(message = "O ID é obrigatório para atualização")
        UUID id,

        @NotNull(message = "O ID Externo é obrigatório")
        Long idExterno,

        @NotBlank(message = "O título é obrigatório")
        @Size(max = 255, message = "O título deve ter no máximo 255 caracteres")
        String titulo,

        String capaUrl,
        String descricao,
        Integer anoLancamento,
        String plataformas,
        String genero
) {}