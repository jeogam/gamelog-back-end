package br.com.ifba.gamelog.features.biblioteca.dto.request;

import br.com.ifba.gamelog.features.biblioteca.model.StatusJogo;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record BibliotecaCriarRequestDTO(
        @NotNull(message = "O ID do usuário é obrigatório")
        UUID usuarioId,

        @NotNull(message = "O ID do jogo é obrigatório")
        UUID jogoId,

        @NotNull(message = "O status é obrigatório")
        StatusJogo status,

        boolean favorito
) {}