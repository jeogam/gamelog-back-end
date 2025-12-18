package br.com.ifba.gamelog.features.perfil.service;

import br.com.ifba.gamelog.features.perfil.dto.request.PerfilAtualizarRequestDTO;
import br.com.ifba.gamelog.features.perfil.dto.request.PerfilCriarRequestDTO;
import br.com.ifba.gamelog.features.perfil.dto.response.PerfilResponseDTO;
import br.com.ifba.gamelog.features.perfil.model.Perfil;
import br.com.ifba.gamelog.features.perfil.repository.IPerfilRepository;
import br.com.ifba.gamelog.features.usuario.model.Usuario;
import br.com.ifba.gamelog.features.usuario.repository.IUsuarioRepository;
import br.com.ifba.gamelog.infrastructure.exception.BusinessException;
import br.com.ifba.gamelog.infrastructure.exception.BusinessExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PerfilService implements IPerfilService {

    private final IPerfilRepository perfilRepository;
    private final IUsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public PerfilResponseDTO save(PerfilCriarRequestDTO dto) {
        // 1. Verificar se usuário existe (UUID)
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new BusinessException("Usuário não encontrado."));

        // 2. Verificar se usuário já tem perfil (Regra 1:1)
        if (perfilRepository.existsByUsuarioId(dto.usuarioId())) {
            throw new BusinessException("Este usuário já possui um perfil cadastrado.");
        }

        // 3. Criar Perfil
        Perfil perfil = new Perfil();
        perfil.setUsuario(usuario);
        perfil.setNomeExibicao(dto.nomeExibicao());
        perfil.setBiografia(dto.biografia());
        perfil.setAvatarImagem(dto.avatarImagem());

        Perfil salvo = perfilRepository.save(perfil);
        return mapToResponse(salvo);
    }

    @Override
    @Transactional(readOnly = true)
    public PerfilResponseDTO findById(Long id) { // Recebe Long
        return perfilRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));
    }

    @Override
    @Transactional(readOnly = true)
    public PerfilResponseDTO findByUsuarioId(UUID usuarioId) { // Recebe UUID
        return perfilRepository.findByUsuarioId(usuarioId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new BusinessException("Perfil não encontrado para o usuário informado."));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PerfilResponseDTO> findAllPaged(Pageable pageable) {
        return perfilRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional
    public PerfilResponseDTO update(PerfilAtualizarRequestDTO dto) {
        // Busca por ID (Long)
        Perfil perfil = perfilRepository.findById(dto.id())
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));

        perfil.setNomeExibicao(dto.nomeExibicao());
        perfil.setBiografia(dto.biografia());
        perfil.setAvatarImagem(dto.avatarImagem());

        Perfil atualizado = perfilRepository.save(perfil);
        return mapToResponse(atualizado);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!perfilRepository.existsById(id)) {
            throw new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage());
        }
        perfilRepository.deleteById(id);
    }

    private PerfilResponseDTO mapToResponse(Perfil entity) {
        return new PerfilResponseDTO(
                entity.getId(), // Long
                entity.getNomeExibicao(),
                entity.getBiografia(),
                entity.getAvatarImagem(),
                entity.getUsuario().getId() // UUID
        );
    }
}