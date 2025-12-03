package br.com.ifba.gamelog.features.usuario.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UsuarioAtualizarRequestDTO(
        @NotNull(message = "O ID é obrigatório para atualização")
        UUID id,

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O email deve ser válido")
        String email,

        // A senha é opcional na atualização (pode ser null se não quiser trocar)
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String senha
) {}