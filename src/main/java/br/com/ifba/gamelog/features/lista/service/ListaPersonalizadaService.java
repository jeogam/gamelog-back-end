package br.com.ifba.gamelog.features.lista.service;

import br.com.ifba.gamelog.features.jogo.dto.response.JogoResponseDTO;
import br.com.ifba.gamelog.features.jogo.model.Jogo;
import br.com.ifba.gamelog.features.jogo.repository.IJogoRepository;
import br.com.ifba.gamelog.features.lista.dto.request.ListaPersonalizadaAtualizarRequestDTO;
import br.com.ifba.gamelog.features.lista.dto.request.ListaPersonalizadaCriarRequestDTO;
import br.com.ifba.gamelog.features.lista.dto.response.ListaPersonalizadaResponseDTO;
import br.com.ifba.gamelog.features.lista.model.ListaPersonalizada;
import br.com.ifba.gamelog.features.lista.repository.IListaPersonalizadaRepository;
import br.com.ifba.gamelog.features.usuario.model.Usuario;
import br.com.ifba.gamelog.features.usuario.repository.IUsuarioRepository;
import br.com.ifba.gamelog.infrastructure.exception.BusinessException;
import br.com.ifba.gamelog.infrastructure.exception.BusinessExceptionMessage;
import br.com.ifba.gamelog.infrastructure.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page; // NOVO IMPORT
import org.springframework.data.domain.Pageable; // NOVO IMPORT
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável por gerenciar listas personalizadas de jogos (ex: "Top 10 RPGs").
 *
 * @author Seu Nome
 */
@Service
@RequiredArgsConstructor
public class ListaPersonalizadaService implements IListaPersonalizadaService {

    private final IListaPersonalizadaRepository repository;
    private final IUsuarioRepository usuarioRepository;
    private final IJogoRepository jogoRepository;
    private final ObjectMapperUtil objectMapperUtil;

    /**
     * Cria uma nova lista, associando ao usuário e opcionalmente adicionando jogos.
     *
     * @param dto Dados da nova lista.
     * @return A lista criada.
     */
    @Override
    @Transactional
    public ListaPersonalizadaResponseDTO save(ListaPersonalizadaCriarRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new BusinessException("Usuário não encontrado."));

        ListaPersonalizada entity = new ListaPersonalizada();
        entity.setNome(dto.nome());
        entity.setPublica(dto.publica());
        entity.setUsuario(usuario);

        // Se vieram IDs de jogos, busca e adiciona à lista
        if (dto.jogosIds() != null && !dto.jogosIds().isEmpty()) {
            List<Jogo> jogos = jogoRepository.findAllById(dto.jogosIds());
            entity.setJogos(jogos);
        } else {
            entity.setJogos(new ArrayList<>());
        }

        ListaPersonalizada savedEntity = repository.save(entity);
        return mapToResponse(savedEntity);
    }

    /**
     * Retorna todas as listas do sistema.
     *
     * @return Lista de DTOs.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ListaPersonalizadaResponseDTO> findAll() {
        return repository.findAll().stream().map(this::mapToResponse).toList();
    }

    /**
     * Retorna todas as listas do sistema com suporte a paginação.
     *
     * @param pageable Configurações de paginação (página, tamanho, ordenação).
     * @return Uma página de DTOs de listas.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ListaPersonalizadaResponseDTO> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable)
                .map(this::mapToResponse);
    }

    /**
     * Retorna as listas de um usuário específico.
     *
     * @param usuarioId ID do dono das listas.
     * @return Listas encontradas.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ListaPersonalizadaResponseDTO> findByUsuario(UUID usuarioId) {
        return repository.findAllByUsuarioId(usuarioId).stream().map(this::mapToResponse).toList();
    }

    /**
     * Busca detalhes de uma lista pelo ID.
     *
     * @param id ID da lista.
     * @return Detalhes da lista.
     */
    @Override
    @Transactional(readOnly = true)
    public ListaPersonalizadaResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));
    }

    /**
     * Atualiza o nome, visibilidade e os jogos da lista.
     *
     * @param dto Dados para atualização.
     * @return Lista atualizada.
     */
    @Override
    @Transactional
    public ListaPersonalizadaResponseDTO update(ListaPersonalizadaAtualizarRequestDTO dto) {
        ListaPersonalizada entity = repository.findById(dto.id())
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));

        entity.setNome(dto.nome());
        entity.setPublica(dto.publica());

        // Atualiza a lista de jogos APENAS se a lista de IDs foi enviada no JSON.
        // Se for null, mantemos a lista antiga. Se for vazia [], limpamos a lista.
        if (dto.jogosIds() != null) {
            List<Jogo> jogos = jogoRepository.findAllById(dto.jogosIds());
            entity.setJogos(jogos);
        }

        ListaPersonalizada updatedEntity = repository.save(entity);
        return mapToResponse(updatedEntity);
    }

    /**
     * Remove uma lista do sistema.
     *
     * @param id ID da lista a ser removida.
     * @return O UUID da lista removida.
     */
    @Override
    @Transactional
    public UUID delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage());
        }
        repository.deleteById(id);
        return id;
    }

    // Mapper manual para lidar com a lista de Jogos dentro da resposta
    private ListaPersonalizadaResponseDTO mapToResponse(ListaPersonalizada entity) {
        // Converte a lista de entidades Jogo para JogoResponseDTO
        List<JogoResponseDTO> jogosDTO = objectMapperUtil.mapAll(entity.getJogos(), JogoResponseDTO.class);

        return new ListaPersonalizadaResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.isPublica(),
                entity.getUsuario().getId(),
                jogosDTO
        );
    }
}