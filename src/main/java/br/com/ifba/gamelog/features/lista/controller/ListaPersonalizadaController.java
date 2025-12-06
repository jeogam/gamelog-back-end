package br.com.ifba.gamelog.features.lista.controller;

import br.com.ifba.gamelog.features.lista.dto.request.ListaPersonalizadaAtualizarRequestDTO;
import br.com.ifba.gamelog.features.lista.dto.request.ListaPersonalizadaCriarRequestDTO;
import br.com.ifba.gamelog.features.lista.dto.response.ListaPersonalizadaResponseDTO;
import br.com.ifba.gamelog.features.lista.dto.response.ListaPersonalizadaRetornarIdResponseDTO;
import br.com.ifba.gamelog.features.lista.service.IListaPersonalizadaService;
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
 * Controlador REST responsável pelo gerenciamento de Listas Personalizadas.
 * <p>
 * Permite criar, editar, listar e remover listas de jogos criadas pelos usuários.
 * </p>
 *
 * @author Seu Nome
 * @since 1.0
 * @see IListaPersonalizadaService
 */
@RestController
@RequestMapping("/api/v1/listas")
@Tag(name = "Gestão de Listas Personalizadas", description = "Listas criadas pelos usuários (ex: 'Top 10 RPGs')")
@RequiredArgsConstructor
public class ListaPersonalizadaController {

    private final IListaPersonalizadaService listaService;

    /**
     * Cria uma nova lista personalizada.
     *
     * @param dto    Dados da lista (nome, pública, usuário, jogos).
     * @param result Resultado da validação.
     * @return A lista criada.
     */
    @Operation(summary = "Criar Lista", description = "Cria uma nova lista de jogos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lista criada com sucesso."),
            @ApiResponse(responseCode = "422", description = "Erro de validação ou usuário inexistente.")
    })
    @PostMapping(value = "/lista", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody @Valid ListaPersonalizadaCriarRequestDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ResultError.getResultErrors(result));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(listaService.save(dto));
    }

    /**
     * Recupera todas as listas cadastradas no sistema (Não paginado).
     *
     * @return Lista de todas as listas.
     */
    @Operation(summary = "Listar Todas", description = "Recupera todas as listas do sistema. Use /paginado para grandes volumes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listas recuperadas com sucesso.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ListaPersonalizadaResponseDTO.class))))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ListaPersonalizadaResponseDTO>> findAll() {
        return ResponseEntity.ok(listaService.findAll());
    }

    /**
     * Recupera todas as listas cadastradas no sistema com paginação.
     *
     * @param pageable Parâmetros de paginação (page, size, sort).
     * @return Uma página de listas.
     */
    @Operation(summary = "Listar Paginado", description = "Recupera todas as listas do sistema com paginação (padrão: 10 itens por página, ordenado por nome).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página recuperada com sucesso.",
                    content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping(value = "/paginado", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ListaPersonalizadaResponseDTO>> findAllPaged(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(listaService.findAllPaged(pageable));
    }


    /**
     * Recupera os detalhes de uma lista específica.
     *
     * @param id UUID da lista.
     * @return Detalhes da lista e seus jogos.
     */
    @Operation(summary = "Buscar por ID", description = "Recupera uma lista específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista encontrada."),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada.")
    })
    @GetMapping("/lista/{id}")
    public ResponseEntity<ListaPersonalizadaResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(listaService.findById(id));
    }

    /**
     * Recupera as listas criadas por um usuário específico.
     *
     * @param usuarioId UUID do usuário.
     * @return Listas desse usuário.
     */
    @Operation(summary = "Listas do Usuário", description = "Recupera as listas criadas por um usuário específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listas recuperadas com sucesso.")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ListaPersonalizadaResponseDTO>> findByUsuario(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(listaService.findByUsuario(usuarioId));
    }

    /**
     * Atualiza uma lista existente.
     *
     * @param id     UUID da lista.
     * @param dto    Novos dados.
     * @param result Validação.
     * @return Lista atualizada.
     */
    @Operation(summary = "Atualizar Lista", description = "Atualiza nome, visibilidade ou os jogos da lista.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista atualizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada."),
            @ApiResponse(responseCode = "422", description = "Erro de validação.")
    })
    @PutMapping("/lista/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @RequestBody @Valid ListaPersonalizadaAtualizarRequestDTO dto,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ResultError.getResultErrors(result));
        }
        if (!id.equals(dto.id())) {
            throw new BusinessException(BusinessExceptionMessage.ID_MISMATCH.getMessage());
        }
        return ResponseEntity.ok(listaService.update(dto));
    }

    /**
     * Remove uma lista do sistema.
     *
     * @param id UUID da lista.
     * @return ID da lista removida.
     */
    @Operation(summary = "Excluir Lista", description = "Remove uma lista do sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista removida com sucesso."),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada.")
    })
    @DeleteMapping("/lista/{id}")
    public ResponseEntity<ListaPersonalizadaRetornarIdResponseDTO> delete(@PathVariable UUID id) {
        UUID idDeletado = listaService.delete(id);
        return ResponseEntity.ok(new ListaPersonalizadaRetornarIdResponseDTO(idDeletado));
    }
}