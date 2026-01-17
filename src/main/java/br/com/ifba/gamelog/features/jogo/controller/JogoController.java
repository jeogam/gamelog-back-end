package br.com.ifba.gamelog.features.jogo.controller;

import br.com.ifba.gamelog.features.jogo.client.RawgApiClient;
import br.com.ifba.gamelog.features.jogo.client.RawgGameDetailResponse;
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
 * Controlador REST responsável pelo gerenciamento de jogos do catálogo.
 * <p>
 * Expõe endpoints para adicionar, listar, atualizar e excluir jogos.
 * Inclui funcionalidade de busca externa (RAWG) e listagem paginada.
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
    private final RawgApiClient rawgApiClient;

    /**
     * Adiciona um novo jogo ao catálogo, utilizando o ID Externo para buscar dados na RAWG.
     */
    @Operation(summary = "Adicionar Jogo", description = "Cadastra um novo jogo no catálogo. O ID Externo deve ser único e é usado para buscar metadados na RAWG.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Jogo cadastrado com sucesso."),
            @ApiResponse(responseCode = "422", description = "Erro de validação ou ID Externo duplicado."),
            @ApiResponse(responseCode = "404", description = "Dados do jogo não encontrados na API externa.")
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
     * Recupera todos os jogos do catálogo (sem paginação).
     */
    @Operation(summary = "Listar Todos os Jogos (Não Paginado)", description = "Recupera uma lista de todos os jogos cadastrados. Use /paginado para grandes volumes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = JogoResponseDTO.class))))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<JogoResponseDTO>> findAll() {
        return ResponseEntity.ok(jogoService.findAll());
    }

    /**
     * Recupera jogos do catálogo com paginação e ordenação.
     */
    @Operation(summary = "Listar Jogos Paginado", description = "Recupera uma lista de jogos com paginação e ordenação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página recuperada com sucesso.",
                    content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping(value = "/paginado", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<JogoResponseDTO>> findAllPaged(
            @PageableDefault(size = 10, sort = "titulo") Pageable pageable) {
        return ResponseEntity.ok(jogoService.findAllPaged(pageable));
    }

    /**
     * Pesquisa jogos na API Externa (RAWG) pelo nome.
     */
    @Operation(summary = "Pesquisar Jogos Externos", description = "Busca jogos na API RAWG. A resposta pode ser usada para selecionar um jogo para importação (POST /jogo).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pesquisa realizada com sucesso.")
    })
    @GetMapping(value = "/pesquisar-externo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RawgGameDetailResponse>> searchExternalGames(@RequestParam String nome) {
        // Uso de .block() em controller síncrono para simplificar a integração com WebClient reativo.
        List<RawgGameDetailResponse> results = rawgApiClient.searchGames(nome).block();
        return ResponseEntity.ok(results);
    }

    /**
     * Busca detalhes de um jogo na API Externa (RAWG) pelo ID Externo.
     * Útil para exibir prévia antes de importar.
     */
    @Operation(summary = "Detalhes Jogo Externo", description = "Busca detalhes de um jogo na RAWG pelo ID Externo (sem precisar salvá-lo no banco local).")
    @GetMapping(value = "/externo/{idExterno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RawgGameDetailResponse> findExternalById(@PathVariable Long idExterno) {
        RawgGameDetailResponse response = rawgApiClient.getGameById(idExterno).block();
        return ResponseEntity.ok(response);
    }

    /**
     * Busca um jogo pelo seu ID interno.
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