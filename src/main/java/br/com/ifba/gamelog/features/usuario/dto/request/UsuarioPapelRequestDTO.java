package br.com.ifba.gamelog.features.usuario.dto.request;

import br.com.ifba.gamelog.features.usuario.model.UsuarioRole;
import jakarta.validation.constraints.NotNull;

public record UsuarioPapelRequestDTO(
        @NotNull(message = "O papel é obrigatório")
        UsuarioRole papel
) {}