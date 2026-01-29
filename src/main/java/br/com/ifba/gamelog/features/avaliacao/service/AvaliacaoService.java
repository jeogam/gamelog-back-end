package br.com.ifba.gamelog.features.avaliacao.service;

import br.com.ifba.gamelog.features.avaliacao.dto.request.AvaliacaoAtualizarRequestDTO;
import br.com.ifba.gamelog.features.avaliacao.dto.request.AvaliacaoCriarRequestDTO;
import br.com.ifba.gamelog.features.avaliacao.dto.response.AvaliacaoResponseDTO;
import br.com.ifba.gamelog.features.avaliacao.mapper.AvaliacaoMapper;
import br.com.ifba.gamelog.features.avaliacao.model.Avaliacao;
import br.com.ifba.gamelog.features.avaliacao.repository.IAvaliacaoRepository;
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
 * Serviço responsável pelas regras de negócio das avaliações.
 * Gerencia a criação, atualização, exclusão e listagem de reviews de jogos.
 *
 * @author Seu Nome
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class AvaliacaoService implements IAvaliacaoService {

    private final IAvaliacaoRepository repository;
    private final IUsuarioRepository usuarioRepository;
    private final IJogoRepository jogoRepository;
    private final AvaliacaoMapper mapper;

    /**
     * Cria uma nova avaliação no sistema.
     *
     * @param dto Dados da nova avaliação.
     * @return DTO da avaliação criada.
     * @throws BusinessException se Usuário/Jogo não existirem ou se já houver avaliação duplicada.
     */
    @Override
    @Transactional
    public AvaliacaoResponseDTO save(AvaliacaoCriarRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.USER_NOT_FOUND.getMessage()));

        Jogo jogo = jogoRepository.findById(dto.jogoId())
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));

        if (repository.existsByUsuarioIdAndJogoId(dto.usuarioId(), dto.jogoId())) {
            throw new BusinessException("Este usuário já avaliou este jogo. Use a edição para alterar a nota.");
        }

        Avaliacao entity = new Avaliacao();
        entity.setNota(dto.nota());
        entity.setComentario(dto.comentario());
        entity.setUsuario(usuario);
        entity.setJogo(jogo);

        Avaliacao savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }

    /**
     * Lista todas as avaliações cadastradas no sistema.
     *
     * @return Lista de DTOs.
     */
    @Override
    @Transactional(readOnly = true)
    public List<AvaliacaoResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * Lista as avaliações do sistema com suporte a paginação.
     *
     * @param pageable Configurações de paginação.
     * @return Uma página de DTOs de avaliações.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AvaliacaoResponseDTO> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    /**
     * Lista todas as avaliações de um jogo específico.
     *
     * @param jogoId ID do jogo.
     * @return Lista de avaliações do jogo.
     */
    @Override
    @Transactional(readOnly = true)
    public List<AvaliacaoResponseDTO> findAllByJogo(UUID jogoId) {
        return repository.findByJogoId(jogoId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * Busca uma avaliação por ID.
     *
     * @param id Identificador da avaliação.
     * @return Dados da avaliação.
     * @throws BusinessException caso não encontrada.
     */
    @Override
    @Transactional(readOnly = true)
    public AvaliacaoResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));
    }

    /**
     * Atualiza nota ou comentário de uma avaliação.
     *
     * @param dto Dados atualizados.
     * @return Avaliação atualizada.
     */
    @Override
    @Transactional
    public AvaliacaoResponseDTO update(AvaliacaoAtualizarRequestDTO dto) {
        Avaliacao avaliacao = repository.findById(dto.id())
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));

        avaliacao.setNota(dto.nota());
        avaliacao.setComentario(dto.comentario());

        Avaliacao updatedEntity = repository.save(avaliacao);
        return mapper.toResponse(updatedEntity);
    }

    /**
     * Exclui uma avaliação.
     *
     * @param id Identificador da avaliação.
     * @return O ID da avaliação excluída.
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