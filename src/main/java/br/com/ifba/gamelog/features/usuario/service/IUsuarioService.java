package br.com.ifba.gamelog.features.usuario.service;

import br.com.ifba.gamelog.features.usuario.dto.request.UsuarioAtualizarRequestDTO;
import br.com.ifba.gamelog.features.usuario.dto.request.UsuarioCriarRequestDTO;
import br.com.ifba.gamelog.features.usuario.dto.response.UsuarioResponseDTO;
import org.springframework.data.domain.Page; // Adicionado import
import org.springframework.data.domain.Pageable; // Adicionado import

import java.util.List;
import java.util.UUID;

public interface IUsuarioService {
    UsuarioResponseDTO save(UsuarioCriarRequestDTO dto);
    List<UsuarioResponseDTO> findAll();

    /**
     * Lista os usuários com suporte a paginação.
     * @param pageable Parâmetros de paginação.
     * @return Uma página de DTOs.
     */
    Page<UsuarioResponseDTO> findAllPaged(Pageable pageable); // NOVO MÉTODO

    UsuarioResponseDTO findById(UUID id);
    UsuarioResponseDTO update(UsuarioAtualizarRequestDTO dto);
    UUID delete(UUID id);
}