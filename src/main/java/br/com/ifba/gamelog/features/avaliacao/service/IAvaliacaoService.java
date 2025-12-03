package br.com.ifba.gamelog.features.avaliacao.service;

import br.com.ifba.gamelog.features.avaliacao.dto.request.AvaliacaoAtualizarRequestDTO;
import br.com.ifba.gamelog.features.avaliacao.dto.request.AvaliacaoCriarRequestDTO;
import br.com.ifba.gamelog.features.avaliacao.dto.response.AvaliacaoResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IAvaliacaoService {
    AvaliacaoResponseDTO save(AvaliacaoCriarRequestDTO dto);
    List<AvaliacaoResponseDTO> findAll();
    AvaliacaoResponseDTO findById(UUID id);
    AvaliacaoResponseDTO update(AvaliacaoAtualizarRequestDTO dto);
    UUID delete(UUID id);
}