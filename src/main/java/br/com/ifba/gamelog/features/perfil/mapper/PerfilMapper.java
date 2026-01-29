package br.com.ifba.gamelog.features.perfil.mapper;

import br.com.ifba.gamelog.features.perfil.dto.response.PerfilResponseDTO;
import br.com.ifba.gamelog.features.perfil.model.Perfil;
import org.springframework.stereotype.Component;

@Component
public class PerfilMapper {

    /**
     * Converte a entidade Perfil para o DTO de resposta.
     * Mapeia manualmente o ID do Usuário para evitar loops ou carregamento desnecessário.
     */
    public PerfilResponseDTO toResponse(Perfil entity) {
        if (entity == null) return null;

        return new PerfilResponseDTO(
                entity.getId(),
                entity.getNomeExibicao(),
                entity.getBiografia(),
                entity.getAvatarImagem(),
                entity.getUsuario() != null ? entity.getUsuario().getId() : null
        );
    }
}