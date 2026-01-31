package br.com.ifba.gamelog.features.perfil.mapper;

import br.com.ifba.gamelog.features.perfil.dto.request.PerfilCriarRequestDTO;
import br.com.ifba.gamelog.features.perfil.dto.response.PerfilResponseDTO;
import br.com.ifba.gamelog.features.perfil.model.Perfil;
import br.com.ifba.gamelog.infrastructure.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PerfilMapper {

    private final ObjectMapperUtil objectMapperUtil;

    /**
     * Converte a entidade Perfil para o DTO de resposta.
     * Mapeia manualmente para suportar o Record e resolver o ID e Papel do usuário de forma segura.
     */
    public PerfilResponseDTO toResponse(Perfil entity) {
        if (entity == null) return null;

        // ✅ Lógica para extrair o papel (Role)
        String papel = "USUARIO";
        if (entity.getUsuario() != null && entity.getUsuario().getPapel() != null) {
            papel = entity.getUsuario().getPapel().name();
        }

        return new PerfilResponseDTO(
                entity.getId(),
                entity.getNomeExibicao(),
                entity.getBiografia(),
                entity.getAvatarImagem(),
                entity.getUsuario() != null ? entity.getUsuario().getId() : null,
                papel
        );
    }

    /**
     * Converte o DTO de criação para Entidade.
     * Segue o padrão de usar o ObjectMapperUtil para a entidade (POJO).
     */
    public Perfil toEntity(PerfilCriarRequestDTO dto) {
        return objectMapperUtil.map(dto, Perfil.class);
    }
}