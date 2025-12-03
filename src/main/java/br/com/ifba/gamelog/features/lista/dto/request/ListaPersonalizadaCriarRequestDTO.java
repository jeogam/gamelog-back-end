package br.com.ifba.gamelog.features.lista.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record ListaPersonalizadaCriarRequestDTO(
        @NotBlank(message = "O nome da lista é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        boolean publica,

        @NotNull(message = "O ID do usuário é obrigatório")
        UUID usuarioId,

        // Opcional: Lista de IDs dos jogos para adicionar inicialmente
        List<UUID> jogosIds
) {}