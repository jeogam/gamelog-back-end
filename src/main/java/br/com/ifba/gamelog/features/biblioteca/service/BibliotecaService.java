package br.com.ifba.gamelog.features.biblioteca.service;

import br.com.ifba.gamelog.features.biblioteca.dto.request.BibliotecaAtualizarRequestDTO;
import br.com.ifba.gamelog.features.biblioteca.dto.request.BibliotecaCriarRequestDTO;
import br.com.ifba.gamelog.features.biblioteca.dto.response.BibliotecaResponseDTO;
import br.com.ifba.gamelog.features.biblioteca.model.Biblioteca;
import br.com.ifba.gamelog.features.biblioteca.repository.IBibliotecaRepository;
import br.com.ifba.gamelog.features.jogo.model.Jogo;
import br.com.ifba.gamelog.features.jogo.repository.IJogoRepository;
import br.com.ifba.gamelog.features.usuario.model.Usuario;
import br.com.ifba.gamelog.features.usuario.repository.IUsuarioRepository;
import br.com.ifba.gamelog.infrastructure.exception.BusinessException;
import br.com.ifba.gamelog.infrastructure.exception.BusinessExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page; // NOVO IMPORT
import org.springframework.data.domain.Pageable; // NOVO IMPORT
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável por gerenciar a biblioteca de jogos dos usuários.
 * Implementa as regras de negócio para salvar, atualizar e listar jogos.
 *
 * @author Seu Nome
 */
@Service
@RequiredArgsConstructor
public class BibliotecaService implements IBibliotecaService {

    private final IBibliotecaRepository repository;
    private final IUsuarioRepository usuarioRepository;
    private final IJogoRepository jogoRepository;

    /**
     * Adiciona um jogo à biblioteca.
     * Valida se usuário e jogo existem e impede duplicidade.
     *
     * @param dto Dados para criação.
     * @return Item criado.
     */
    @Override
    @Transactional
    public BibliotecaResponseDTO save(BibliotecaCriarRequestDTO dto) {
        // 1. Validações de existência
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new BusinessException("Usuário não encontrado."));

        Jogo jogo = jogoRepository.findById(dto.jogoId())
                .orElseThrow(() -> new BusinessException("Jogo não encontrado."));

        // 2. Regra: Não pode ter o mesmo jogo 2x na biblioteca
        if (repository.existsByUsuarioIdAndJogoId(dto.usuarioId(), dto.jogoId())) {
            throw new BusinessException("Este jogo já está na biblioteca deste usuário.");
        }

        // 3. Montagem
        Biblioteca entity = new Biblioteca();
        entity.setUsuario(usuario);
        entity.setJogo(jogo);
        entity.setStatus(dto.status());
        entity.setFavorito(dto.favorito());

        Biblioteca savedEntity = repository.save(entity);
        return mapToResponse(savedEntity);
    }

    /**
     * Lista todos os itens de todas as bibliotecas.
     *
     * @return Lista geral.
     */
    @Override
    @Transactional(readOnly = true)
    public List<BibliotecaResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Lista todos os itens de biblioteca do sistema com suporte a paginação.
     *
     * @param pageable Configurações de paginação (página, tamanho, ordenação).
     * @return Uma página de DTOs de itens de biblioteca.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BibliotecaResponseDTO> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable)
                .map(this::mapToResponse);
    }

    /**
     * Lista a biblioteca de um usuário específico.
     *
     * @param usuarioId ID do usuário.
     * @return Lista de jogos do usuário.
     */
    @Override
    @Transactional(readOnly = true)
    public List<BibliotecaResponseDTO> findAllByUsuario(UUID usuarioId) {
        return repository.findAllByUsuarioId(usuarioId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Busca um item pelo ID.
     *
     * @param id ID do item.
     * @return DTO do item.
     */
    @Override
    @Transactional(readOnly = true)
    public BibliotecaResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));
    }

    /**
     * Atualiza status ou favorito de um item.
     *
     * @param dto Dados atualizados.
     * @return Item atualizado.
     */
    @Override
    @Transactional
    public BibliotecaResponseDTO update(BibliotecaAtualizarRequestDTO dto) {
        Biblioteca entity = repository.findById(dto.id())
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));

        entity.setStatus(dto.status());
        entity.setFavorito(dto.favorito());

        Biblioteca updatedEntity = repository.save(entity);
        return mapToResponse(updatedEntity);
    }

    /**
     * Remove um item da biblioteca.
     *
     * @param id ID do item.
     * @return ID removido.
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

    // Método auxiliar privado não precisa de Javadoc obrigatório, mas ajuda a entender
    private BibliotecaResponseDTO mapToResponse(Biblioteca entity) {
        return new BibliotecaResponseDTO(
                entity.getId(),
                entity.getStatus(),
                entity.isFavorito(),
                entity.getUsuario().getId(),
                entity.getJogo().getId(),
                entity.getJogo().getTitulo()
        );
    }
}