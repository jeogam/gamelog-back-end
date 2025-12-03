package br.com.ifba.gamelog.features.usuario.service;

import br.com.ifba.gamelog.features.usuario.dto.request.UsuarioAtualizarRequestDTO;
import br.com.ifba.gamelog.features.usuario.dto.request.UsuarioCriarRequestDTO;
import br.com.ifba.gamelog.features.usuario.dto.response.UsuarioResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IUsuarioService {
    UsuarioResponseDTO save(UsuarioCriarRequestDTO dto);
    List<UsuarioResponseDTO> findAll();
    UsuarioResponseDTO findById(UUID id);
    UsuarioResponseDTO update(UsuarioAtualizarRequestDTO dto);
    UUID delete(UUID id);
}