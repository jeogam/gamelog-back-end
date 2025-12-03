package br.com.ifba.gamelog.features.lista.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record ListaPersonalizadaAtualizarRequestDTO(
        @NotNull(message = "O ID é obrigatório")
        UUID id,

        @NotBlank(message = "O nome da lista é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        boolean publica,

        // Se passar null, mantém os jogos atuais. Se passar lista vazia, remove tudo.
        List<UUID> jogosIds
) {}