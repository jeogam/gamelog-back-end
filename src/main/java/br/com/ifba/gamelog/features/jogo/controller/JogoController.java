package br.com.ifba.gamelog.features.jogo.controller;

import br.com.ifba.gamelog.features.jogo.dto.request.JogoAtualizarRequestDTO;
import br.com.ifba.gamelog.features.jogo.dto.request.JogoCriarRequestDTO;
import br.com.ifba.gamelog.features.jogo.dto.response.JogoResponseDTO;
import br.com.ifba.gamelog.features.jogo.dto.response.JogoRetornarIdResponseDTO;
import br.com.ifba.gamelog.features.jogo.service.IJogoService;
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
 * Controlador REST responsável pelo gerenciamento de jogos do catálogo.
 * <p>
 * Expõe endpoints para adicionar, listar, atualizar e excluir jogos.
 * </p>
 *
 * @author Seu Nome
 * @since 1.0
 * @see IJogoService
 * @see JogoResponseDTO
 */
@RestController
@RequestMapping("/api/v1/jogos")
@Tag(name = "Gestão de Jogos", description = "Endpoints para gerenciamento do catálogo de jogos")
@RequiredArgsConstructor
public class JogoController {

    private final IJogoService jogoService;

    /**
     * Adiciona um novo jogo ao catálogo.
     *
     * @param jogoDto DTO contendo os dados do jogo.
     * @param result  Resultado da validação.
     * @return Um {@link ResponseEntity} com status 201 Created ou erros.
     */
    @Operation(summary = "Adicionar Jogo", description = "Cadastra um novo jogo no catálogo. O ID Externo deve ser único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Jogo cadastrado com sucesso."),
            @ApiResponse(responseCode = "422", description = "Erro de validação ou ID Externo duplicado.")
    })
    @PostMapping(value = "/jogo",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> save(
            @RequestBody @Valid JogoCriarRequestDTO jogoDto,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(ResultError.getResultErrors(result));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jogoService.save(jogoDto));
    }

    /**
     * Recupera todos os jogos do catálogo.
     *
     * @return Lista de jogos.
     */
    @Operation(summary = "Listar Todos os Jogos", description = "Recupera uma lista de todos os jogos cadastrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = JogoResponseDTO.class))))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<JogoResponseDTO>> findAll() {
        return ResponseEntity.ok(jogoService.findAll());
    }

    /**
     * Busca um jogo pelo seu ID interno.
     *
     * @param id O UUID do jogo.
     * @return Detalhes do jogo.
     */
    @Operation(summary = "Buscar Jogo por ID", description = "Recupera detalhes de um jogo específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jogo encontrado.",
                    content = @Content(schema = @Schema(implementation = JogoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Jogo não encontrado.")
    })
    @GetMapping("/jogo/{id}")
    public ResponseEntity<JogoResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(jogoService.findById(id));
    }

    /**
     * Atualiza os dados de um jogo.
     *
     * @param id      UUID do jogo.
     * @param jogoDto Dados atualizados.
     * @param result  Resultado da validação.
     * @return Jogo atualizado.
     */
    @Operation(summary = "Atualizar Jogo", description = "Atualiza propriedades de um jogo existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jogo atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Jogo não encontrado."),
            @ApiResponse(responseCode = "422", description = "Erro de validação ou conflito.")
    })
    @PutMapping("/jogo/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @RequestBody @Valid JogoAtualizarRequestDTO jogoDto,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(ResultError.getResultErrors(result));
        }

        if (!id.equals(jogoDto.id())) {
            throw new BusinessException(BusinessExceptionMessage.ID_MISMATCH.getMessage());
        }

        return ResponseEntity.ok(jogoService.update(jogoDto));
    }

    /**
     * Remove um jogo do sistema.
     *
     * @param id UUID do jogo a ser removido.
     * @return ID removido.
     */
    @Operation(summary = "Excluir Jogo", description = "Remove um jogo do catálogo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jogo excluído com sucesso."),
            @ApiResponse(responseCode = "404", description = "Jogo não encontrado.")
    })
    @DeleteMapping("/jogo/{id}")
    public ResponseEntity<JogoRetornarIdResponseDTO> excluir(@PathVariable UUID id) {
        UUID idDeletado = jogoService.delete(id);
        return ResponseEntity.ok(new JogoRetornarIdResponseDTO(idDeletado));
    }
}