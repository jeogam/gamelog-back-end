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
// Removemos o ObjectMapperUtil pois faremos o mapeamento manual, que é mais seguro para Records
// import br.com.ifba.gamelog.infrastructure.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JogoService implements IJogoService {

    private final IJogoRepository repository;
    // private final ObjectMapperUtil objectMapperUtil; // Não é mais necessário para este Service
    private final RawgApiClient rawgApiClient;

    @Override
    @Transactional
    public JogoResponseDTO save(JogoCriarRequestDTO dto) {
        // Verifica se já existe localmente (Idempotência)
        var jogoExistente = repository.findByIdExterno(dto.idExterno());
        if (jogoExistente.isPresent()) {
            return mapToDTO(jogoExistente.get());
        }

        // Busca na API Externa
        RawgGameDetailResponse externalGame = rawgApiClient.getGameById(dto.idExterno()).block();

        if (externalGame == null) {
            throw new BusinessException("Não foi possível obter os dados do jogo na API RAWG com o ID Externo fornecido.");
        }

        // Mapeia para Entidade
        Jogo entity = new Jogo();
        entity.setIdExterno(externalGame.id());
        entity.setTitulo(externalGame.name());
        entity.setCapaUrl(externalGame.background_image());
        entity.setDescricao(externalGame.description_raw());
        entity.setAnoLancamento(externalGame.getAnoLancamento());
        entity.setPlataformas(dto.plataformas());
        entity.setGenero(dto.genero());

        Jogo savedEntity = repository.save(entity);

        return mapToDTO(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JogoResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JogoResponseDTO> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable)
                .map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public JogoResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));
    }

    @Override
    @Transactional
    public JogoResponseDTO update(JogoAtualizarRequestDTO dto) {
        Jogo jogoExistente = repository.findById(dto.id())
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));

        // Verifica unicidade do ID Externo apenas se ele foi alterado
        if (!jogoExistente.getIdExterno().equals(dto.idExterno()) && repository.existsByIdExterno(dto.idExterno())) {
            throw new BusinessException(BusinessExceptionMessage.ATTRIBUTE_VALUE_ALREADY_EXISTS.getAttributeValueAlreadyExistsMessage("ID Externo"));
        }

        // Atualiza campos
        jogoExistente.setIdExterno(dto.idExterno());
        jogoExistente.setTitulo(dto.titulo());
        jogoExistente.setCapaUrl(dto.capaUrl());
        jogoExistente.setDescricao(dto.descricao());
        jogoExistente.setAnoLancamento(dto.anoLancamento());
        jogoExistente.setPlataformas(dto.plataformas());
        jogoExistente.setGenero(dto.genero());

        Jogo updatedEntity = repository.save(jogoExistente);
        return mapToDTO(updatedEntity);
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

    /**
     * Método auxiliar para converter Entidade -> Record DTO manualmente.
     * Resolve o erro de "no-argument constructor" do ModelMapper.
     */
    private JogoResponseDTO mapToDTO(Jogo entity) {
        return new JogoResponseDTO(
                entity.getId(),
                entity.getIdExterno(),
                entity.getTitulo(),
                entity.getCapaUrl(),
                entity.getDescricao(),
                entity.getAnoLancamento(),
                entity.getPlataformas(),
                entity.getGenero()
        );
    }
}