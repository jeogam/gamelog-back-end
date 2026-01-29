package br.com.ifba.gamelog.features.usuario.controller;

import br.com.ifba.gamelog.features.usuario.dto.request.UsuarioPapelRequestDTO;
import br.com.ifba.gamelog.features.usuario.dto.response.UsuarioResponseDTO;
import br.com.ifba.gamelog.features.usuario.service.IUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controlador REST administrativo para gestão avançada de usuários.
 * <p>
 * Contém operações sensíveis, como alteração de papéis (Role).
 * </p>
 *
 * @author Seu Nome
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1/admin/usuarios")
@Tag(name = "Admin - Gestão de Usuários", description = "Endpoints administrativos para gestão de usuários")
@RequiredArgsConstructor
public class AdminUsuarioController {

    private final IUsuarioService usuarioService;

    /**
     * Altera o papel (Role) de um usuário (ex: USUARIO -> ADMINISTRADOR).
     * Requer permissão de administrador.
     *
     * @param id  UUID do usuário.
     * @param dto Novo papel.
     * @return Usuário com papel atualizado.
     */
    @Operation(summary = "Alterar Papel do Usuário", description = "Endpoint exclusivo para Administradores alterarem o papel de um usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Papel atualizado com sucesso."),
            @ApiResponse(responseCode = "403", description = "Acesso negado (Não é ADMIN)."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
    })
    @PutMapping("/{id}/papel")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponseDTO> updatePapel(
            @PathVariable UUID id,
            @RequestBody @Valid UsuarioPapelRequestDTO dto) {

        return ResponseEntity.ok(usuarioService.updatePapel(id, dto));
    }
}