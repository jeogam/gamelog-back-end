package br.com.ifba.gamelog.features.biblioteca.dto.request;

import br.com.ifba.gamelog.features.biblioteca.model.StatusJogo;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record BibliotecaAtualizarRequestDTO(
        @NotNull(message = "O ID do item da biblioteca é obrigatório")
        UUID id,

        @NotNull(message = "O status é obrigatório")
        StatusJogo status,

        boolean favorito
) {}