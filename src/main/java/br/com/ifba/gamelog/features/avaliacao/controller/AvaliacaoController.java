package br.com.ifba.gamelog.features.avaliacao.controller;

import br.com.ifba.gamelog.features.avaliacao.dto.request.AvaliacaoAtualizarRequestDTO;
import br.com.ifba.gamelog.features.avaliacao.dto.request.AvaliacaoCriarRequestDTO;
import br.com.ifba.gamelog.features.avaliacao.dto.response.AvaliacaoResponseDTO;
import br.com.ifba.gamelog.features.avaliacao.dto.response.AvaliacaoRetornarIdResponseDTO;
import br.com.ifba.gamelog.features.avaliacao.service.IAvaliacaoService;
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
import org.springframework.data.domain.Page; // NOVO IMPORT
import org.springframework.data.domain.Pageable; // NOVO IMPORT
import org.springframework.data.web.PageableDefault; // NOVO IMPORT
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para gerenciamento de avaliações (reviews) de jogos.
 *
 * @author Seu Nome
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1/avaliacoes")
@Tag(name = "Gestão de Avaliações", description = "Endpoints para criar, editar e listar avaliações de jogos")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final IAvaliacaoService avaliacaoService;

    /**
     * Cria uma nova avaliação para um jogo.
     *
     * @param dto Dados da avaliação (nota, comentário, usuário e jogo).
     * @param result Resultado da validação.
     * @return A avaliação criada.
     */
    @Operation(summary = "Criar Avaliação", description = "Registra uma nota e comentário de um usuário para um jogo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avaliação criada com sucesso."),
            @ApiResponse(responseCode = "422", description = "Erro de validação ou usuário já avaliou este jogo."),
            @ApiResponse(responseCode = "404", description = "Usuário ou Jogo não encontrados.")
    })
    @PostMapping(value = "/avaliacao",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> save(
            @RequestBody @Valid AvaliacaoCriarRequestDTO dto,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(ResultError.getResultErrors(result));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(avaliacaoService.save(dto));
    }

    /**
     * Lista todas as avaliações do sistema (Não paginado).
     *
     * @return Lista de avaliações.
     */
    @Operation(summary = "Listar Avaliações", description = "Recupera todas as avaliações cadastradas. Use /paginado para grandes volumes.")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AvaliacaoResponseDTO>> findAll() {
        return ResponseEntity.ok(avaliacaoService.findAll());
    }

    /**
     * Recupera avaliações do sistema com paginação e ordenação.
     *
     * @param pageable Parâmetros de paginação (page, size, sort).
     * @return Uma página de avaliações.
     */
    @Operation(summary = "Listar Avaliações Paginado", description = "Recupera uma lista de avaliações com paginação e ordenação (padrão: 10 itens por página, ordenado por data de criação).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página recuperada com sucesso.",
                    content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping(value = "/paginado", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<AvaliacaoResponseDTO>> findAllPaged(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(avaliacaoService.findAllPaged(pageable));
    }


    /**
     * Busca uma avaliação pelo ID.
     *
     * @param id UUID da avaliação.
     * @return Detalhes da avaliação.
     */
    @Operation(summary = "Buscar Avaliação por ID", description = "Recupera uma avaliação específica.")
    @GetMapping("/avaliacao/{id}")
    public ResponseEntity<AvaliacaoResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(avaliacaoService.findById(id));
    }

    /**
     * Atualiza uma avaliação existente (nota ou comentário).
     *
     * @param id UUID da avaliação.
     * @param dto Novos dados.
     * @param result Validação.
     * @return Avaliação atualizada.
     */
    @Operation(summary = "Atualizar Avaliação", description = "Atualiza nota ou comentário de uma avaliação existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliação atualizada."),
            @ApiResponse(responseCode = "422", description = "Nota inválida ou erro de validação.")
    })
    @PutMapping("/avaliacao/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @RequestBody @Valid AvaliacaoAtualizarRequestDTO dto,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(ResultError.getResultErrors(result));
        }

        if (!id.equals(dto.id())) {
            throw new BusinessException(BusinessExceptionMessage.ID_MISMATCH.getMessage());
        }

        return ResponseEntity.ok(avaliacaoService.update(dto));
    }

    /**
     * Remove uma avaliação.
     *
     * @param id UUID da avaliação.
     * @return ID removido.
     */
    @Operation(summary = "Excluir Avaliação", description = "Remove uma avaliação do sistema.")
    @DeleteMapping("/avaliacao/{id}")
    public ResponseEntity<AvaliacaoRetornarIdResponseDTO> delete(@PathVariable UUID id) {
        UUID idDeletado = avaliacaoService.delete(id);
        return ResponseEntity.ok(new AvaliacaoRetornarIdResponseDTO(idDeletado));
    }
}