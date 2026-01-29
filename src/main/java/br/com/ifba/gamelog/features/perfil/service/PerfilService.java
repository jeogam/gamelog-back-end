package br.com.ifba.gamelog.features.perfil.service;

import br.com.ifba.gamelog.features.perfil.dto.request.PerfilAtualizarMeusDadosRequestDTO;
import br.com.ifba.gamelog.features.perfil.dto.request.PerfilAtualizarRequestDTO;
import br.com.ifba.gamelog.features.perfil.dto.request.PerfilCriarRequestDTO;
import br.com.ifba.gamelog.features.perfil.dto.response.PerfilResponseDTO;
import br.com.ifba.gamelog.features.perfil.mapper.PerfilMapper;
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

/**
 * Serviço responsável pelas regras de negócio de Perfis.
 * Gerencia criação, atualização e busca de perfis de usuário.
 *
 * @author Seu Nome
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class PerfilService implements IPerfilService {

    private final IPerfilRepository perfilRepository;
    private final IUsuarioRepository usuarioRepository;
    private final PerfilMapper mapper;

    /**
     * Cria um novo perfil para um usuário.
     * Valida se o usuário existe e se já possui perfil.
     */
    @Override
    @Transactional
    public PerfilResponseDTO save(PerfilCriarRequestDTO dto) {
        // 1. Verificar se usuário existe
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.USER_NOT_FOUND.getMessage()));

        // 2. Verificar se usuário já tem perfil (Regra 1:1)
        if (perfilRepository.existsByUsuarioId(dto.usuarioId())) {
            throw new BusinessException(
                    BusinessExceptionMessage.ATTRIBUTE_VALUE_ALREADY_EXISTS.getAttributeValueAlreadyExistsMessage("Perfil para este Usuário")
            );
        }

        // 3. Criar Perfil
        Perfil perfil = new Perfil();
        perfil.setUsuario(usuario);
        perfil.setNomeExibicao(dto.nomeExibicao());
        perfil.setBiografia(dto.biografia());
        perfil.setAvatarImagem(dto.avatarImagem());

        Perfil salvo = perfilRepository.save(perfil);
        return mapper.toResponse(salvo);
    }

    /**
     * Busca perfil por ID.
     */
    @Override
    @Transactional(readOnly = true)
    public PerfilResponseDTO findById(Long id) {
        return perfilRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));
    }

    /**
     * Busca perfil pelo ID do usuário associado.
     */
    @Override
    @Transactional(readOnly = true)
    public PerfilResponseDTO findByUsuarioId(UUID usuarioId) {
        return perfilRepository.findByUsuarioId(usuarioId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));
    }

    /**
     * Lista perfis com paginação.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PerfilResponseDTO> findAllPaged(Pageable pageable) {
        return perfilRepository.findAll(pageable)
                .map(mapper::toResponse);
    }

    /**
     * Atualiza dados de um perfil (Admin/Geral).
     */
    @Override
    @Transactional
    public PerfilResponseDTO update(PerfilAtualizarRequestDTO dto) {
        Perfil perfil = perfilRepository.findById(dto.id())
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));

        perfil.setNomeExibicao(dto.nomeExibicao());
        perfil.setBiografia(dto.biografia());
        perfil.setAvatarImagem(dto.avatarImagem());

        Perfil atualizado = perfilRepository.save(perfil);
        return mapper.toResponse(atualizado);
    }

    /**
     * Remove um perfil.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        if (!perfilRepository.existsById(id)) {
            throw new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage());
        }
        perfilRepository.deleteById(id);
    }

    /**
     * Atualiza os dados do perfil do usuário logado.
     */
    @Override
    @Transactional
    public PerfilResponseDTO updateByUsuarioId(UUID usuarioId, PerfilAtualizarMeusDadosRequestDTO dto) {
        Perfil perfil = perfilRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new BusinessException(BusinessExceptionMessage.NOT_FOUND.getMessage()));

        perfil.setNomeExibicao(dto.nomeExibicao());
        perfil.setBiografia(dto.biografia());
        perfil.setAvatarImagem(dto.avatarImagem());

        Perfil updatedEntity = perfilRepository.save(perfil);
        return mapper.toResponse(updatedEntity);
    }
}