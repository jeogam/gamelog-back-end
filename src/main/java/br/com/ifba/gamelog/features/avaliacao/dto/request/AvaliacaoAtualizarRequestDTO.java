package br.com.ifba.gamelog.features.avaliacao.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AvaliacaoAtualizarRequestDTO(
        @NotNull(message = "O ID é obrigatório para atualização")
        UUID id,

        @NotNull(message = "A nota é obrigatória")
        @Min(value = 1, message = "A nota mínima é 1")
        @Max(value = 5, message = "A nota máxima é 5")
        Integer nota,

        @Size(max = 500, message = "O comentário deve ter no máximo 500 caracteres")
        String comentario
) {}