package br.com.ifba.gamelog.features.avaliacao.service;

import br.com.ifba.gamelog.features.avaliacao.dto.request.AvaliacaoAtualizarRequestDTO;
import br.com.ifba.gamelog.features.avaliacao.dto.request.AvaliacaoCriarRequestDTO;
import br.com.ifba.gamelog.features.avaliacao.dto.response.AvaliacaoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; // Adicionado import

import java.util.List;
import java.util.UUID;

public interface IAvaliacaoService {
    AvaliacaoResponseDTO save(AvaliacaoCriarRequestDTO dto);
    List<AvaliacaoResponseDTO> findAll();

    /**
     * Lista as avaliações com suporte a paginação.
     * @param pageable Parâmetros de paginação.
     * @return Uma página de DTOs.
     */
    Page<AvaliacaoResponseDTO> findAllPaged(Pageable pageable); // NOVO MÉTODO

    AvaliacaoResponseDTO findById(UUID id);
    AvaliacaoResponseDTO update(AvaliacaoAtualizarRequestDTO dto);
    UUID delete(UUID id);
}