package br.com.ifba.gamelog.features.usuario.controller;

import br.com.ifba.gamelog.features.usuario.dto.request.UsuarioAtualizarRequestDTO;
import br.com.ifba.gamelog.features.usuario.dto.request.UsuarioCriarRequestDTO;
import br.com.ifba.gamelog.features.usuario.dto.response.UsuarioResponseDTO;
import br.com.ifba.gamelog.features.usuario.dto.response.UsuarioRetornarIdResponseDTO;
import br.com.ifba.gamelog.features.usuario.service.IUsuarioService;
import br.com.ifba.gamelog.infrastructure.exception.BusinessException;
import br.com.ifba.gamelog.infrastructure.exception.BusinessExceptionMessage;
import br.com.ifba.gamelog.infrastructure.util.ResultError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST responsável pelo gerenciamento de usuários do GameLog.
 * <p>
 * Expõe endpoints para cadastro, listagem, atualização e exclusão de usuários (Gamers).
 * </p>
 *
 * @author Seu Nome
 * @since 1.0
 * @see IUsuarioService
 * @see UsuarioResponseDTO
 */
@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Gestão de Usuários", description = "Endpoints para gerenciamento dos usuários do GameLog")
@RequiredArgsConstructor
public class UsuarioController {

    private final IUsuarioService usuarioService;

    /**
     * Registra um novo usuário no sistema.
     *
     * @param usuarioDto DTO contendo nome, email e senha do usuário.
     * @param result     Resultado da validação dos campos obrigatórios.
     * @return Um {@link ResponseEntity} com status 201 Created ou erros de validação.
     */
    @Operation(summary = "Criar Novo Usuário", description = "Cria um novo usuário no sistema. O email deve ser único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso."),
            @ApiResponse(responseCode = "422", description = "Erro de validação ou Email já existente.")
    })
    @PostMapping(value = "/usuario",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> save(
            @RequestBody @Valid UsuarioCriarRequestDTO usuarioDto,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(ResultError.getResultErrors(result));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.save(usuarioDto));
    }

    /**
     * Recupera a lista de todos os usuários cadastrados (Não paginado).
     *
     * @return Uma lista de DTOs de resposta de usuários.
     */
    @Operation(summary = "Listar Todos os Usuários", description = "Recupera uma lista de todos os usuários cadastrados. Use /paginado para grandes volumes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDTO.class))))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UsuarioResponseDTO>> findAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    /**
     * Recupera uma lista de usuários com paginação e ordenação.
     *
     * @param pageable Parâmetros de paginação (page, size, sort).
     * @return Uma página de DTOs de resposta de usuários.
     */
    @Operation(summary = "Listar Usuários Paginado", description = "Recupera uma lista de usuários com paginação e ordenação (padrão: 10 itens por página, ordenado por nome).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página recuperada com sucesso.",
                    content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping(value = "/paginado", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<UsuarioResponseDTO>> findAllPaged(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(usuarioService.findAllPaged(pageable));
    }

    /**
     * Busca os detalhes de um usuário pelo seu ID único (UUID).
     *
     * @param id O UUID do usuário.
     * @return Os dados do usuário encontrado.
     */
    @Operation(summary = "Buscar Usuário por ID", description = "Recupera detalhes de um usuário específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado.",
                    content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
    })
    @GetMapping("/usuario/{id}")
    public ResponseEntity<UsuarioResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    /**
     * Atualiza os dados cadastrais de um usuário.
     *
     * @param id         O UUID do usuário a ser atualizado.
     * @param usuarioDto DTO com os novos dados (nome, email, senha).
     * @param result     Resultado da validação.
     * @return O usuário atualizado.
     */
    @Operation(summary = "Atualizar Usuário", description = "Atualiza dados de um usuário existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
            @ApiResponse(responseCode = "422", description = "Erro de validação ou conflito de dados (email duplicado).")
    })
    @PutMapping("/usuario/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @RequestBody @Valid UsuarioAtualizarRequestDTO usuarioDto,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(ResultError.getResultErrors(result));
        }

        // Validação de consistência: ID da URL == ID do Corpo
        if (!id.equals(usuarioDto.id())) {
            throw new BusinessException(BusinessExceptionMessage.ID_MISMATCH.getMessage());
        }

        return ResponseEntity.ok(usuarioService.update(usuarioDto));
    }

    /**
     * Remove permanentemente um usuário do sistema.
     *
     * @param id O UUID do usuário a ser excluído.
     * @return O UUID do usuário que foi removido.
     */
    @Operation(summary = "Excluir Usuário", description = "Remove um usuário do sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário excluído com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
    })
    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<UsuarioRetornarIdResponseDTO> excluir(@PathVariable UUID id) {
        UUID idDeletado = usuarioService.delete(id);
        return ResponseEntity.ok(new UsuarioRetornarIdResponseDTO(idDeletado));
    }
}