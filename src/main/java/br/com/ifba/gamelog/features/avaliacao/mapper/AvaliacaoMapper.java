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
     * Converte Entidade Avaliacao para ResponseDTO.
     */
    public AvaliacaoResponseDTO toResponse(Avaliacao entity) {
        // Mapeamento manual para garantir os IDs aninhados (Usuario e Jogo)
        // O ModelMapper as vezes se perde com Lazy Loading se não configurado,
        // então essa abordagem híbrida é mais segura.
        return new AvaliacaoResponseDTO(
                entity.getId(),
                entity.getNota(),
                entity.getComentario(),
                entity.getUsuario().getId(),
                entity.getJogo().getId()
        );
    }

    /**
     * Converte DTO de criação para Entidade.
     */
    public Avaliacao toEntity(AvaliacaoCriarRequestDTO dto) {
        return objectMapperUtil.map(dto, Avaliacao.class);
    }
}