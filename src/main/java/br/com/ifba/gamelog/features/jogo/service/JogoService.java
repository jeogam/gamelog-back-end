package br.com.ifba.gamelog.features.jogo.service;

import br.com.ifba.gamelog.features.jogo.dto.request.JogoAtualizarRequestDTO;
import br.com.ifba.gamelog.features.jogo.dto.request.JogoCriarRequestDTO;
import br.com.ifba.gamelog.features.jogo.dto.response.JogoResponseDTO;
import br.com.ifba.gamelog.features.jogo.mapper.JogoMapper;
import br.com.ifba.gamelog.features.jogo.model.Jogo;
import br.com.ifba.gamelog.features.jogo.repository.IJogoRepository;
import br.com.ifba.gamelog.infrastructure.exception.BusinessException;
import br.com.ifba.gamelog.infrastructure.exception.BusinessExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável pelas regras de negócio de Jogos.
 * Gerencia o catálogo, importação e atualização de metadados.
 *
 * @author Seu Nome
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class JogoService implements IJogoService {

    private final IJogoRepository repository;
    private final JogoMapper mapper;

    /**
     * Cadastra um novo jogo no banco de dados.
     * Verifica se o ID Externo já existe para evitar duplicatas.
     */
    @Override
    @Transactional
    public JogoResponseDTO save(JogoCriarRequestDTO dto) {
        // Validação de unicidade do ID Externo (se fornecido)
        if (dto.idExterno() != null && repository.existsByIdExterno(dto.idExterno())) {
            throw new BusinessException(
                    BusinessExceptionMessage.ATTRIBUTE_VALUE_ALREADY_EXISTS.getAttributeValueAlreadyExistsMessage("ID Externo")
            );
        }

        Jogo entity = mapper.toEntity(dto);
        Jogo savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }

    /**
     * Lista todos os jogos cadastrados.
     */
    @Override
    @Transactional(readOnly = true)
    public List<JogoResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * Lista jogos com paginação.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<JogoResponseDTO> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    /**
     * Busca um jogo por ID.
     */
    @Override
    @Transactional(readOnly = true)
    public JogoResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));
    }

    /**
     * Atualiza dados de um jogo.
     */
    @Override
    @Transactional
    public JogoResponseDTO update(JogoAtualizarRequestDTO dto) {
        Jogo entity = repository.findById(dto.id())
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));

        // Atualização manual dos campos permitidos
        entity.setTitulo(dto.titulo());
        entity.setDescricao(dto.descricao());
        entity.setCapaUrl(dto.capaUrl());
        // Adicione outros campos conforme necessário (genero, desenvolvedor, etc.)

        Jogo updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }

    /**
     * Exclui um jogo do catálogo.
     */
    @Override
    @Transactional
    public UUID delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage());
        }
        // Poderia adicionar validação aqui: "Se o jogo estiver em bibliotecas, não delete"
        // Mas por enquanto, segue a exclusão padrão.
        repository.deleteById(id);
        return id;
    }
}