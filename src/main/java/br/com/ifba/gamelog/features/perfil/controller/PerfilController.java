package br.com.ifba.gamelog.features.perfil.controller;

import br.com.ifba.gamelog.features.perfil.dto.request.PerfilAtualizarRequestDTO;
import br.com.ifba.gamelog.features.perfil.dto.request.PerfilCriarRequestDTO;
import br.com.ifba.gamelog.features.perfil.dto.response.PerfilResponseDTO;
import br.com.ifba.gamelog.features.perfil.service.IPerfilService;
import br.com.ifba.gamelog.infrastructure.exception.BusinessException;
import br.com.ifba.gamelog.infrastructure.exception.BusinessExceptionMessage;
import br.com.ifba.gamelog.infrastructure.util.ResultError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controlador REST responsável pelo gerenciamento de perfis de usuário do GameLog.
 * <p>
 * Expõe endpoints para cadastro, visualização, atualização e remoção de perfis (Avatar, Bio, Nome de Exibição).
 * </p>
 *
 * @author Seu Nome
 * @since 1.0
 * @see IPerfilService
 * @see PerfilResponseDTO
 */
@RestController
@RequestMapping("/api/v1/perfis")
@Tag(name = "Gestão de Perfis", description = "Endpoints para gerenciamento de perfis de usuário (Avatar, Bio)")
@RequiredArgsConstructor
public class PerfilController {

    private final IPerfilService perfilService;

    /**
     * Cria um novo perfil para um usuário.
     *
     * @param dto    DTO contendo ID do usuário, nome de exibição, biografia e avatar.
     * @param result Resultado da validação dos campos.
     * @return Um {@link ResponseEntity} com status 201 Created ou erros de validação.
     */
    @Operation(summary = "Criar Perfil", description = "Cria um perfil (bio, avatar) para um usuário existente. Um usuário só pode ter um perfil.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Perfil criado com sucesso."),
            @ApiResponse(responseCode = "422", description = "Erro de validação ou usuário já possui perfil."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
    })
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> save(@RequestBody @Valid PerfilCriarRequestDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(ResultError.getResultErrors(result));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(perfilService.save(dto));
    }

    /**
     * Busca os detalhes de um perfil pelo seu ID único (Long).
     *
     * @param id O ID do perfil.
     * @return Os dados do perfil encontrado.
     */
    @Operation(summary = "Buscar Perfil por ID", description = "Recupera detalhes de um perfil pelo seu identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil encontrado.",
                    content = @Content(schema = @Schema(implementation = PerfilResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PerfilResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(perfilService.findById(id));
    }

    /**
     * Busca o perfil associado a um usuário específico.
     *
     * @param usuarioId O UUID do usuário dono do perfil.
     * @return Os dados do perfil encontrado.
     */
    @Operation(summary = "Buscar Perfil por Usuário", description = "Recupera o perfil associado a um usuário específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil encontrado."),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado para este usuário.")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<PerfilResponseDTO> findByUsuarioId(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(perfilService.findByUsuarioId(usuarioId));
    }

    /**
     * Recupera uma lista de perfis com paginação.
     *
     * @param pageable Parâmetros de paginação (page, size, sort).
     * @return Uma página de DTOs de perfis.
     */
    @Operation(summary = "Listar Perfis Paginado", description = "Recupera uma lista de perfis com paginação (padrão: 10 itens por página).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página recuperada com sucesso.",
                    content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping(value = "/paginado", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PerfilResponseDTO>> findAllPaged(
            @PageableDefault(size = 10, sort = "nomeExibicao") Pageable pageable) {
        return ResponseEntity.ok(perfilService.findAllPaged(pageable));
    }

    /**
     * Atualiza os dados de um perfil existente.
     *
     * @param id     O ID do perfil a ser atualizado.
     * @param dto    DTO com os novos dados.
     * @param result Resultado da validação.
     * @return O perfil atualizado.
     */
    @Operation(summary = "Atualizar Perfil", description = "Atualiza bio, nome de exibição ou avatar de um perfil existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado."),
            @ApiResponse(responseCode = "422", description = "Erro de validação.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody @Valid PerfilAtualizarRequestDTO dto,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(ResultError.getResultErrors(result));
        }

        // Validação de consistência: ID da URL == ID do Corpo
        if (!id.equals(dto.id())) {
            throw new BusinessException(BusinessExceptionMessage.ID_MISMATCH.getMessage());
        }

        return ResponseEntity.ok(perfilService.update(dto));
    }

    /**
     * Remove um perfil do sistema.
     *
     * @param id O ID do perfil a ser excluído.
     * @return Response vazio com status 200 OK.
     */
    @Operation(summary = "Excluir Perfil", description = "Remove um perfil do sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil excluído com sucesso."),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        perfilService.delete(id);
        return ResponseEntity.ok().build();
    }
}