package br.com.ifba.gamelog.features.lista.service;

import br.com.ifba.gamelog.features.jogo.model.Jogo;
import br.com.ifba.gamelog.features.jogo.repository.IJogoRepository;
import br.com.ifba.gamelog.features.lista.dto.request.ListaPersonalizadaAtualizarRequestDTO;
import br.com.ifba.gamelog.features.lista.dto.request.ListaPersonalizadaCriarRequestDTO;
import br.com.ifba.gamelog.features.lista.dto.response.ListaPersonalizadaResponseDTO;
import br.com.ifba.gamelog.features.lista.mapper.ListaPersonalizadaMapper;
import br.com.ifba.gamelog.features.lista.model.ListaPersonalizada;
import br.com.ifba.gamelog.features.lista.repository.IListaPersonalizadaRepository;
import br.com.ifba.gamelog.features.usuario.model.Usuario;
import br.com.ifba.gamelog.features.usuario.repository.IUsuarioRepository;
import br.com.ifba.gamelog.infrastructure.exception.BusinessException;
import br.com.ifba.gamelog.infrastructure.exception.BusinessExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável por gerenciar listas personalizadas de jogos (ex: "Top 10 RPGs").
 *
 * @author Seu Nome
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class ListaPersonalizadaService implements IListaPersonalizadaService {

    private final IListaPersonalizadaRepository repository;
    private final IUsuarioRepository usuarioRepository;
    private final IJogoRepository jogoRepository;
    private final ListaPersonalizadaMapper mapper;

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
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.USER_NOT_FOUND.getMessage()));

        ListaPersonalizada entity = new ListaPersonalizada();
        entity.setNome(dto.nome());
        entity.setPublica(dto.publica());
        entity.setUsuario(usuario);

        // Se vieram IDs de jogos, busca e adiciona à lista
        if (dto.jogosIds() != null && !dto.jogosIds().isEmpty()) {
            List<Jogo> jogos = jogoRepository.findAllById(dto.jogosIds());
            // Opcional: Validar se todos os jogos foram encontrados
            entity.setJogos(jogos);
        } else {
            entity.setJogos(new ArrayList<>());
        }

        ListaPersonalizada savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }

    /**
     * Retorna todas as listas do sistema.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ListaPersonalizadaResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * Retorna todas as listas do sistema com suporte a paginação.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ListaPersonalizadaResponseDTO> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    /**
     * Retorna as listas de um usuário específico.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ListaPersonalizadaResponseDTO> findByUsuario(UUID usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new BusinessException(BusinessExceptionMessage.USER_NOT_FOUND.getMessage());
        }
        return repository.findAllByUsuarioId(usuarioId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * Busca detalhes de uma lista pelo ID.
     */
    @Override
    @Transactional(readOnly = true)
    public ListaPersonalizadaResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));
    }

    /**
     * Atualiza o nome, visibilidade e os jogos da lista.
     */
    @Override
    @Transactional
    public ListaPersonalizadaResponseDTO update(ListaPersonalizadaAtualizarRequestDTO dto) {
        ListaPersonalizada entity = repository.findById(dto.id())
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));

        entity.setNome(dto.nome());
        entity.setPublica(dto.publica());

        // Atualiza a lista de jogos APENAS se a lista de IDs foi enviada no JSON (não nula).
        if (dto.jogosIds() != null) {
            List<Jogo> jogos = jogoRepository.findAllById(dto.jogosIds());
            entity.setJogos(jogos);
        }

        ListaPersonalizada updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }

    /**
     * Remove uma lista do sistema.
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
}