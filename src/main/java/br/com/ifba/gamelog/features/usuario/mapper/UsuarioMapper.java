package br.com.ifba.gamelog.features.usuario.mapper;

import br.com.ifba.gamelog.features.usuario.dto.request.UsuarioCriarRequestDTO;
import br.com.ifba.gamelog.features.usuario.dto.response.UsuarioResponseDTO;
import br.com.ifba.gamelog.features.usuario.model.Usuario;
import br.com.ifba.gamelog.infrastructure.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioMapper {

    private final ObjectMapperUtil objectMapperUtil;

    /**
     * Converte a entidade Usuario para o DTO de resposta.
     */
    public UsuarioResponseDTO toResponse(Usuario entity) {
        if (entity == null) return null;

        // ✅ Busca avatar do perfil, se existir
        String avatarImagem = null;
        if (entity.getPerfil() != null) {
            avatarImagem = entity.getPerfil().getAvatarImagem();
        }

        return new UsuarioResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getPapel(),
                avatarImagem // ✅ Mapeado
        );
    }

    /**
     * Converte o DTO de criação para a entidade Usuario.
     */
    public Usuario toEntity(UsuarioCriarRequestDTO dto) {
        return objectMapperUtil.map(dto, Usuario.class);
    }
}