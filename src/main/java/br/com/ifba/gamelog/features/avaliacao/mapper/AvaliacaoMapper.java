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

    public AvaliacaoResponseDTO toResponse(Avaliacao entity) {
        // Esta abordagem manual é a mais segura para Records no Java 21
        return new AvaliacaoResponseDTO(
                entity.getId(),
                entity.getNota(),
                entity.getComentario(),
                entity.getUsuario().getId(), // Extrai o ID do Usuario associado
                entity.getJogo().getId()      // Extrai o ID do Jogo associado
        );
    }

    public Avaliacao toEntity(AvaliacaoCriarRequestDTO dto) {
        // Funciona porque Avaliacao.class é um POJO com construtor padrão
        return objectMapperUtil.map(dto, Avaliacao.class);
    }
}