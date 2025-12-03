package br.com.ifba.gamelog.features.avaliacao.service;

import br.com.ifba.gamelog.features.avaliacao.dto.request.AvaliacaoAtualizarRequestDTO;
import br.com.ifba.gamelog.features.avaliacao.dto.request.AvaliacaoCriarRequestDTO;
import br.com.ifba.gamelog.features.avaliacao.dto.response.AvaliacaoResponseDTO;
import br.com.ifba.gamelog.features.avaliacao.model.Avaliacao;
import br.com.ifba.gamelog.features.avaliacao.repository.IAvaliacaoRepository;
import br.com.ifba.gamelog.features.jogo.model.Jogo;
import br.com.ifba.gamelog.features.jogo.repository.IJogoRepository;
import br.com.ifba.gamelog.features.usuario.model.Usuario;
import br.com.ifba.gamelog.features.usuario.repository.IUsuarioRepository;
import br.com.ifba.gamelog.infrastructure.exception.BusinessException;
import br.com.ifba.gamelog.infrastructure.exception.BusinessExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável pelas regras de negócio das avaliações.
 * Gerencia a criação, atualização e listagem de reviews de jogos.
 *
 * @author Seu Nome
 */
@Service
@RequiredArgsConstructor
public class AvaliacaoService implements IAvaliacaoService {

    private final IAvaliacaoRepository repository;
    private final IUsuarioRepository usuarioRepository; // Necessário para buscar o autor
    private final IJogoRepository jogoRepository;       // Necessário para buscar o jogo

    /**
     * Cria uma nova avaliação no sistema.
     * <p>
     * Valida se o usuário e o jogo existem e se o usuário já não avaliou este jogo anteriormente.
     * </p>
     *
     * @param dto Dados da nova avaliação.
     * @return DTO da avaliação criada.
     * @throws BusinessException se Usuário/Jogo não existirem ou se já houver avaliação duplicada.
     */
    @Override
    @Transactional
    public AvaliacaoResponseDTO save(AvaliacaoCriarRequestDTO dto) {
        // 1. Validar existência do Usuário
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new BusinessException("Usuário não encontrado com o ID fornecido."));

        // 2. Validar existência do Jogo
        Jogo jogo = jogoRepository.findById(dto.jogoId())
                .orElseThrow(() -> new BusinessException("Jogo não encontrado com o ID fornecido."));

        // 3. Regra de Negócio: Evitar duplicidade
        if (repository.existsByUsuarioIdAndJogoId(dto.usuarioId(), dto.jogoId())) {
            throw new BusinessException("Este usuário já avaliou este jogo. Use a edição para alterar a nota.");
        }

        // 4. Montagem da Entidade
        Avaliacao entity = new Avaliacao();
        entity.setNota(dto.nota());
        entity.setComentario(dto.comentario());
        entity.setUsuario(usuario);
        entity.setJogo(jogo);

        Avaliacao savedEntity = repository.save(entity);
        return mapToResponse(savedEntity);
    }

    /**
     * Lista todas as avaliações cadastradas.
     *
     * @return Lista de DTOs.
     */
    @Override
    @Transactional(readOnly = true)
    public List<AvaliacaoResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Busca uma avaliação por ID.
     *
     * @param id Identificador da avaliação.
     * @return Dados da avaliação.
     */
    @Override
    @Transactional(readOnly = true)
    public AvaliacaoResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(this::mapToResponse)
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
        return mapToResponse(updatedEntity);
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

    // Método auxiliar para converter Entity -> ResponseDTO manualmente para garantir os IDs
    // Isso evita problemas de Lazy Loading e garante que o retorno tenha os IDs corretos
    private AvaliacaoResponseDTO mapToResponse(Avaliacao entity) {
        return new AvaliacaoResponseDTO(
                entity.getId(),
                entity.getNota(),
                entity.getComentario(),
                entity.getUsuario().getId(),
                entity.getJogo().getId()
        );
    }
}