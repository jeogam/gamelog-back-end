package br.com.ifba.gamelog.features.lista.mapper;

import br.com.ifba.gamelog.features.jogo.dto.response.JogoResponseDTO;
import br.com.ifba.gamelog.features.lista.dto.response.ListaPersonalizadaResponseDTO;
import br.com.ifba.gamelog.features.lista.model.ListaPersonalizada;
import br.com.ifba.gamelog.infrastructure.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListaPersonalizadaMapper {

    private final ObjectMapperUtil objectMapperUtil;

    /**
     * Converte a entidade ListaPersonalizada para o DTO de resposta.
     * Inclui a conversão da lista de jogos contida nela.
     */
    public ListaPersonalizadaResponseDTO toResponse(ListaPersonalizada entity) {
        if (entity == null) return null;

        // Converte a lista de entidades Jogo para JogoResponseDTO
        List<JogoResponseDTO> jogosDTO = objectMapperUtil.mapAll(entity.getJogos(), JogoResponseDTO.class);

        return new ListaPersonalizadaResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.isPublica(),
                entity.getUsuario().getId(),
                jogosDTO
        );
    }
}