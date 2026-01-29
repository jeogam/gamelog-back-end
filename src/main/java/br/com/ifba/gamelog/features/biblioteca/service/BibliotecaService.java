package br.com.ifba.gamelog.features.biblioteca.service;

import br.com.ifba.gamelog.features.biblioteca.dto.request.BibliotecaAtualizarRequestDTO;
import br.com.ifba.gamelog.features.biblioteca.dto.request.BibliotecaCriarRequestDTO;
import br.com.ifba.gamelog.features.biblioteca.dto.response.BibliotecaResponseDTO;
import br.com.ifba.gamelog.features.biblioteca.mapper.BibliotecaMapper;
import br.com.ifba.gamelog.features.biblioteca.model.Biblioteca;
import br.com.ifba.gamelog.features.biblioteca.repository.IBibliotecaRepository;
import br.com.ifba.gamelog.features.jogo.model.Jogo;
import br.com.ifba.gamelog.features.jogo.repository.IJogoRepository;
import br.com.ifba.gamelog.features.usuario.model.Usuario;
import br.com.ifba.gamelog.features.usuario.repository.IUsuarioRepository;
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
 * Serviço responsável pelas regras de negócio da biblioteca de jogos.
 * Gerencia a adição, atualização, remoção e listagem de jogos salvos pelos usuários.
 *
 * @author Seu Nome
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class BibliotecaService implements IBibliotecaService {

    private final IBibliotecaRepository repository;
    private final IUsuarioRepository usuarioRepository;
    private final IJogoRepository jogoRepository;
    private final BibliotecaMapper mapper;

    /**
     * Adiciona um jogo à biblioteca do usuário.
     *
     * @param dto Dados para inclusão na biblioteca.
     * @return O item criado.
     * @throws BusinessException Se usuário/jogo não existirem ou se já houver duplicidade.
     */
    @Override
    @Transactional
    public BibliotecaResponseDTO save(BibliotecaCriarRequestDTO dto) {
        // 1. Validar Usuário
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.USER_NOT_FOUND.getMessage()));

        // 2. Validar Jogo
        Jogo jogo = jogoRepository.findById(dto.jogoId())
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));

        // 3. Validar Duplicidade
        if (repository.existsByUsuarioIdAndJogoId(dto.usuarioId(), dto.jogoId())) {
            throw new BusinessException(
                    BusinessExceptionMessage.ATTRIBUTE_VALUE_ALREADY_EXISTS.getAttributeValueAlreadyExistsMessage("Jogo na Biblioteca")
            );
        }

        // 4. Salvar
        Biblioteca entity = new Biblioteca();
        entity.setUsuario(usuario);
        entity.setJogo(jogo);
        entity.setStatus(dto.status());
        entity.setFavorito(dto.favorito());

        Biblioteca savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }

    /**
     * Lista todos os itens de biblioteca cadastrados no sistema.
     */
    @Override
    @Transactional(readOnly = true)
    public List<BibliotecaResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * Lista itens de biblioteca com paginação.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BibliotecaResponseDTO> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    /**
     * Lista todos os jogos na biblioteca de um usuário específico.
     */
    @Override
    @Transactional(readOnly = true)
    public List<BibliotecaResponseDTO> findAllByUsuario(UUID usuarioId) {
        // Valida se o usuário existe antes de buscar (opcional, mas boa prática)
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new BusinessException(BusinessExceptionMessage.USER_NOT_FOUND.getMessage());
        }

        return repository.findAllByUsuarioId(usuarioId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * Busca um item específico da biblioteca por ID.
     */
    @Override
    @Transactional(readOnly = true)
    public BibliotecaResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));
    }

    /**
     * Atualiza o status ou favorito de um item da biblioteca.
     */
    @Override
    @Transactional
    public BibliotecaResponseDTO update(BibliotecaAtualizarRequestDTO dto) {
        Biblioteca entity = repository.findById(dto.id())
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));

        entity.setStatus(dto.status());
        entity.setFavorito(dto.favorito());

        Biblioteca updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }

    /**
     * Remove um item da biblioteca.
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