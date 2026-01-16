package br.com.ifba.gamelog.features.jogo.service;

import br.com.ifba.gamelog.features.jogo.client.RawgApiClient;
import br.com.ifba.gamelog.features.jogo.client.RawgGameDetailResponse;
import br.com.ifba.gamelog.features.jogo.dto.request.JogoAtualizarRequestDTO;
import br.com.ifba.gamelog.features.jogo.dto.request.JogoCriarRequestDTO;
import br.com.ifba.gamelog.features.jogo.dto.response.JogoResponseDTO;
import br.com.ifba.gamelog.features.jogo.model.Jogo;
import br.com.ifba.gamelog.features.jogo.repository.IJogoRepository;
import br.com.ifba.gamelog.infrastructure.exception.BusinessException;
import br.com.ifba.gamelog.infrastructure.exception.BusinessExceptionMessage;
import br.com.ifba.gamelog.infrastructure.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional; 
import java.util.UUID;

/**
 * Serviço responsável pela lógica de negócio dos jogos.
 *
 * @author JeoGam
 */
@Service
@RequiredArgsConstructor
public class JogoService implements IJogoService {

    private final IJogoRepository repository;
    private final ObjectMapperUtil objectMapperUtil;
    private final RawgApiClient rawgApiClient;

    /**
     * Cadastra um novo jogo.
     * Implementa Idempotência: Se o jogo já existir (pelo ID Externo), retorna o existente.
     * Caso contrário, busca metadados na RAWG e salva um novo.
     *
     * @param dto Dados do novo jogo.
     * @return DTO do jogo salvo ou recuperado.
     */
    @Override
    @Transactional
    public JogoResponseDTO save(JogoCriarRequestDTO dto) {
        // 1. Idempotência: Verifica se já existe no banco local pelo ID Externo
        Optional<Jogo> jogoExistente = repository.findByIdExterno(dto.idExterno());

        if (jogoExistente.isPresent()) {
            // Se existir, retorna o jogo do banco sem chamar a API externa ou lançar erro
            return objectMapperUtil.map(jogoExistente.get(), JogoResponseDTO.class);
        }

        // 2. Se não existe, busca os dados completos na API RAWG
        RawgGameDetailResponse externalGame = rawgApiClient.getGameById(dto.idExterno()).block();

        if (externalGame == null) {
            throw new BusinessException("Não foi possível obter os dados do jogo na API RAWG com o ID Externo fornecido.");
        }

        // 3. Mapeamento da resposta da API externa para a Entidade interna
        Jogo entity = new Jogo();
        entity.setIdExterno(externalGame.id());
        entity.setTitulo(externalGame.name());
        entity.setCapaUrl(externalGame.background_image());
        entity.setDescricao(externalGame.description_raw());
        entity.setAnoLancamento(externalGame.getAnoLancamento());
        entity.setPlataformas(dto.plataformas());
        entity.setGenero(dto.genero());

        Jogo savedEntity = repository.save(entity);

        return objectMapperUtil.map(savedEntity, JogoResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JogoResponseDTO> findAll() {
        return objectMapperUtil.mapAll(repository.findAll(), JogoResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JogoResponseDTO> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable)
                .map(entity -> objectMapperUtil.map(entity, JogoResponseDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public JogoResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(entity -> objectMapperUtil.map(entity, JogoResponseDTO.class))
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));
    }

    @Override
    @Transactional
    public JogoResponseDTO update(JogoAtualizarRequestDTO dto) {
        Jogo jogoExistente = repository.findById(dto.id())
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));

        // Verifica se mudou o ID Externo e se o novo já existe em outro jogo
        if (!jogoExistente.getIdExterno().equals(dto.idExterno()) && repository.existsByIdExterno(dto.idExterno())) {
            throw new BusinessException(BusinessExceptionMessage.ATTRIBUTE_VALUE_ALREADY_EXISTS.getAttributeValueAlreadyExistsMessage("ID Externo"));
        }

        jogoExistente.setIdExterno(dto.idExterno());
        jogoExistente.setTitulo(dto.titulo());
        jogoExistente.setCapaUrl(dto.capaUrl());
        jogoExistente.setDescricao(dto.descricao());
        jogoExistente.setAnoLancamento(dto.anoLancamento());
        jogoExistente.setPlataformas(dto.plataformas());
        jogoExistente.setGenero(dto.genero());

        Jogo updatedEntity = repository.save(jogoExistente);
        return objectMapperUtil.map(updatedEntity, JogoResponseDTO.class);
    }

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