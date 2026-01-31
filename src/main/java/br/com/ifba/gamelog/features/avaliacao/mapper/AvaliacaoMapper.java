package br.com.ifba.gamelog.features.avaliacao.mapper;

import br.com.ifba.gamelog.features.avaliacao.dto.request.AvaliacaoCriarRequestDTO;
import br.com.ifba.gamelog.features.avaliacao.dto.response.AvaliacaoResponseDTO;
import br.com.ifba.gamelog.features.avaliacao.model.Avaliacao;
import br.com.ifba.gamelog.infrastructure.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AvaliacaoMapper {

    private final ObjectMapperUtil objectMapperUtil;

    /**
     * Converte a entidade Avaliacao para o DTO de resposta.
     * Mapeamento manual para suportar Java Records (imutáveis) e regras de negócio de exibição.
     */
    public AvaliacaoResponseDTO toResponse(Avaliacao entity) {
        if (entity == null) return null;

        String nomeExibicao = null;
        String avatarImagem = null;

        // Verifica se existe usuário associado
        if (entity.getUsuario() != null) {
            // Define o nome padrão como o nome do usuário (fallback)
            nomeExibicao = entity.getUsuario().getNome();

            // Se houver perfil, tenta pegar os dados personalizados
            if (entity.getUsuario().getPerfil() != null) {
                if (entity.getUsuario().getPerfil().getNomeExibicao() != null) {
                    nomeExibicao = entity.getUsuario().getPerfil().getNomeExibicao();
                }
                avatarImagem = entity.getUsuario().getPerfil().getAvatarImagem();
            }
        }

        return new AvaliacaoResponseDTO(
                entity.getId(),
                entity.getNota(),
                entity.getComentario(),
                entity.getUsuario() != null ? entity.getUsuario().getId() : null,
                entity.getJogo() != null ? entity.getJogo().getId() : null,
                nomeExibicao,
                avatarImagem,
                entity.getCreatedAt()
        );
    }

    /**
     * Converte o DTO de criação para a entidade Avaliacao.
     * Aqui usamos o utilitário, pois a entidade Avaliacao é um POJO padrão.
     */
    public Avaliacao toEntity(AvaliacaoCriarRequestDTO dto) {
        return objectMapperUtil.map(dto, Avaliacao.class);
    }
}