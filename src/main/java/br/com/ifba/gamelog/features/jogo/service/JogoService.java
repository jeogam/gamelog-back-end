package br.com.ifba.gamelog.features.jogo.service;

import br.com.ifba.gamelog.features.jogo.dto.request.JogoAtualizarRequestDTO;
import br.com.ifba.gamelog.features.jogo.dto.request.JogoCriarRequestDTO;
import br.com.ifba.gamelog.features.jogo.dto.response.JogoResponseDTO;
import br.com.ifba.gamelog.features.jogo.model.Jogo;
import br.com.ifba.gamelog.features.jogo.repository.IJogoRepository;
import br.com.ifba.gamelog.infrastructure.exception.BusinessException;
import br.com.ifba.gamelog.infrastructure.exception.BusinessExceptionMessage;
import br.com.ifba.gamelog.infrastructure.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável pela lógica de negócio dos jogos.
 *
 * @author Seu Nome
 */
@Service
@RequiredArgsConstructor
public class JogoService implements IJogoService {

    private final IJogoRepository repository;
    private final ObjectMapperUtil objectMapperUtil;

    /**
     * Cadastra um novo jogo, verificando se o ID Externo já existe.
     *
     * @param dto Dados do novo jogo.
     * @return DTO do jogo salvo.
     */
    @Override
    @Transactional
    public JogoResponseDTO save(JogoCriarRequestDTO dto) {
        if (repository.existsByIdExterno(dto.idExterno())) {
            throw new BusinessException(BusinessExceptionMessage.ATTRIBUTE_VALUE_ALREADY_EXISTS.getAttributeValueAlreadyExistsMessage("ID Externo"));
        }

        Jogo entity = objectMapperUtil.map(dto, Jogo.class);
        Jogo savedEntity = repository.save(entity);

        return objectMapperUtil.map(savedEntity, JogoResponseDTO.class);
    }

    /**
     * Lista todos os jogos cadastrados.
     *
     * @return Lista de jogos.
     */
    @Override
    @Transactional(readOnly = true)
    public List<JogoResponseDTO> findAll() {
        return objectMapperUtil.mapAll(repository.findAll(), JogoResponseDTO.class);
    }

    /**
     * Busca um jogo pelo ID interno (UUID).
     *
     * @param id UUID do jogo.
     * @return Dados do jogo.
     */
    @Override
    @Transactional(readOnly = true)
    public JogoResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(entity -> objectMapperUtil.map(entity, JogoResponseDTO.class))
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));
    }

    /**
     * Atualiza dados de um jogo existente.
     *
     * @param dto Dados atualizados.
     * @return Jogo atualizado.
     */
    @Override
    @Transactional
    public JogoResponseDTO update(JogoAtualizarRequestDTO dto) {
        Jogo jogoExistente = repository.findById(dto.id())
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));

        // Verifica se mudou o ID Externo e se o novo já existe em outro jogo
        if (!jogoExistente.getIdExterno().equals(dto.idExterno()) && repository.existsByIdExterno(dto.idExterno())) {
            throw new BusinessException(BusinessExceptionMessage.ATTRIBUTE_VALUE_ALREADY_EXISTS.getAttributeValueAlreadyExistsMessage("ID Externo"));
        }

        // Atualização manual ou via mapper (aqui manual para garantir controle)
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

    /**
     * Remove um jogo do sistema.
     *
     * @param id UUID do jogo.
     * @return UUID removido.
     */
    @Override
    @Transactional
    public UUID delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage());
        }
        // Atenção: Se houver avaliações ou bibliotecas vinculadas, o banco pode bloquear (ConstraintViolation)
        // a menos que o CascadeType.ALL na entidade Jogo esteja configurado para remover filhos junto.
        // Na sua entidade Jogo, você usou cascade = CascadeType.ALL, então vai apagar tudo que depende do jogo.
        repository.deleteById(id);
        return id;
    }
}