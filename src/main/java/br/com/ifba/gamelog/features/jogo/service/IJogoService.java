package br.com.ifba.gamelog.features.jogo.service;

import br.com.ifba.gamelog.features.jogo.dto.request.JogoAtualizarRequestDTO;
import br.com.ifba.gamelog.features.jogo.dto.request.JogoCriarRequestDTO;
import br.com.ifba.gamelog.features.jogo.dto.response.JogoResponseDTO;
import org.springframework.data.domain.Page; // 👈 NOVO IMPORT
import org.springframework.data.domain.Pageable; // 👈 NOVO IMPORT

import java.util.List;
import java.util.UUID;

public interface IJogoService {
    JogoResponseDTO save(JogoCriarRequestDTO dto);
    List<JogoResponseDTO> findAll();
    Page<JogoResponseDTO> findAllPaged(Pageable pageable);
    JogoResponseDTO findById(UUID id);
    JogoResponseDTO update(JogoAtualizarRequestDTO dto);
    UUID delete(UUID id);
}