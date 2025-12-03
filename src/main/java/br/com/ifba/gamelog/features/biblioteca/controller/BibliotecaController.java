package br.com.ifba.gamelog.features.biblioteca.controller;

import br.com.ifba.gamelog.features.biblioteca.dto.request.BibliotecaAtualizarRequestDTO;
import br.com.ifba.gamelog.features.biblioteca.dto.request.BibliotecaCriarRequestDTO;
import br.com.ifba.gamelog.features.biblioteca.dto.response.BibliotecaResponseDTO;
import br.com.ifba.gamelog.features.biblioteca.dto.response.BibliotecaRetornarIdResponseDTO;
import br.com.ifba.gamelog.features.biblioteca.service.IBibliotecaService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST responsável pelo gerenciamento da biblioteca de jogos dos usuários.
 * <p>
 * Expõe endpoints para adicionar jogos à biblioteca, atualizar status/favoritos, listar e remover itens.
 * </p>
 *
 * @author Seu Nome
 * @since 1.0
 * @see IBibliotecaService
 * @see BibliotecaResponseDTO
 */
@RestController
@RequestMapping("/api/v1/bibliotecas")
@Tag(name = "Gestão de Biblioteca", description = "Gerencia os jogos salvos pelos usuários (Status, Favoritos)")
@RequiredArgsConstructor
public class BibliotecaController {

    private final IBibliotecaService bibliotecaService;

    /**
     * Adiciona um novo jogo à biblioteca do usuário.
     *
     * @param dto    DTO contendo os IDs de usuário e jogo, além do status inicial.
     * @param result Resultado da validação.
     * @return O item criado na biblioteca.
     */
    @Operation(summary = "Adicionar à Biblioteca", description = "Adiciona um jogo na biblioteca do usuário com um status inicial.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Adicionado com sucesso."),
            @ApiResponse(responseCode = "422", description = "Usuário já possui este jogo na biblioteca ou erro de validação.")
    })
    @PostMapping(value = "/item",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(
            @RequestBody @Valid BibliotecaCriarRequestDTO dto,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(ResultError.getResultErrors(result));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bibliotecaService.save(dto));
    }

    /**
     * Recupera todos os itens de biblioteca cadastrados no sistema (Visão Admin).
     *
     * @return Lista completa de itens de biblioteca.
     */
    @Operation(summary = "Listar Tudo", description = "Lista todos os itens de biblioteca do sistema (Admin).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BibliotecaResponseDTO.class))))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BibliotecaResponseDTO>> findAll() {
        return ResponseEntity.ok(bibliotecaService.findAll());
    }

    /**
     * Recupera a biblioteca completa de um usuário específico.
     *
     * @param usuarioId UUID do usuário.
     * @return Lista de jogos na biblioteca desse usuário.
     */
    @Operation(summary = "Listar por Usuário", description = "Lista todos os jogos na biblioteca de um usuário específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista do usuário recuperada com sucesso.")
    })
    @GetMapping(value = "/usuario/{usuarioId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BibliotecaResponseDTO>> findAllByUsuario(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(bibliotecaService.findAllByUsuario(usuarioId));
    }

    /**
     * Busca um item específico da biblioteca pelo seu ID.
     *
     * @param id UUID do item da biblioteca.
     * @return Detalhes do item (Status, Favorito, Jogo).
     */
    @Operation(summary = "Buscar por ID", description = "Detalhes de um item específico da biblioteca.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item encontrado."),
            @ApiResponse(responseCode = "404", description = "Item não encontrado.")
    })
    @GetMapping("/item/{id}")
    public ResponseEntity<BibliotecaResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(bibliotecaService.findById(id));
    }

    /**
     * Atualiza o status ou favorito de um jogo na biblioteca.
     *
     * @param id     UUID do item a ser atualizado.
     * @param dto    Novos dados (Status, Favorito).
     * @param result Resultado da validação.
     * @return O item atualizado.
     */
    @Operation(summary = "Atualizar Status/Favorito", description = "Muda o status (ex: JOGANDO -> ZERADO) ou favorita o jogo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Item não encontrado."),
            @ApiResponse(responseCode = "422", description = "Erro de validação.")
    })
    @PutMapping("/item/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @RequestBody @Valid BibliotecaAtualizarRequestDTO dto,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(ResultError.getResultErrors(result));
        }

        if (!id.equals(dto.id())) {
            throw new BusinessException(BusinessExceptionMessage.ID_MISMATCH.getMessage());
        }

        return ResponseEntity.ok(bibliotecaService.update(dto));
    }

    /**
     * Remove um jogo da biblioteca do usuário.
     *
     * @param id UUID do item da biblioteca a ser removido.
     * @return O ID do item removido.
     */
    @Operation(summary = "Remover da Biblioteca", description = "Remove um jogo da biblioteca do usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Removido com sucesso."),
            @ApiResponse(responseCode = "404", description = "Item não encontrado.")
    })
    @DeleteMapping("/item/{id}")
    public ResponseEntity<BibliotecaRetornarIdResponseDTO> delete(@PathVariable UUID id) {
        UUID idDeletado = bibliotecaService.delete(id);
        return ResponseEntity.ok(new BibliotecaRetornarIdResponseDTO(idDeletado));
    }
}