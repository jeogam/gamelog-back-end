package br.com.ifba.gamelog.features.jogo.mapper;

import br.com.ifba.gamelog.features.jogo.dto.request.JogoCriarRequestDTO;
import br.com.ifba.gamelog.features.jogo.dto.response.JogoResponseDTO;
import br.com.ifba.gamelog.features.jogo.model.Jogo;
import br.com.ifba.gamelog.infrastructure.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JogoMapper {

    private final ObjectMapperUtil objectMapperUtil;

    /**
     * Converte a entidade Jogo para o DTO de resposta.
     */
    public JogoResponseDTO toResponse(Jogo entity) {
        return objectMapperUtil.map(entity, JogoResponseDTO.class);
    }

    /**
     * Converte o DTO de criação para a entidade Jogo.
     */
    public Jogo toEntity(JogoCriarRequestDTO dto) {
        return objectMapperUtil.map(dto, Jogo.class);
    }
}