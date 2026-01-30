package br.com.ifba.gamelog.features.lista.mapper;

import br.com.ifba.gamelog.features.jogo.dto.response.JogoResponseDTO;
import br.com.ifba.gamelog.features.jogo.mapper.JogoMapper;
import br.com.ifba.gamelog.features.lista.dto.request.ListaPersonalizadaCriarRequestDTO;
import br.com.ifba.gamelog.features.lista.dto.response.ListaPersonalizadaResponseDTO;
import br.com.ifba.gamelog.features.lista.model.ListaPersonalizada;
import br.com.ifba.gamelog.infrastructure.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ListaPersonalizadaMapper {

    private final ObjectMapperUtil objectMapperUtil;
    private final JogoMapper jogoMapper; // Injeção essencial para converter a lista de Records

    /**
     * Converte a entidade ListaPersonalizada para o DTO de resposta.
     */
    public ListaPersonalizadaResponseDTO toResponse(ListaPersonalizada entity) {
        if (entity == null) return null;

        // Usa o JogoMapper para converter cada jogo da lista.
        // Isso evita erros de reflexão, pois o JogoMapper já sabe instanciar o Record JogoResponseDTO.
        List<JogoResponseDTO> jogosDTO = entity.getJogos() == null ? Collections.emptyList() :
                entity.getJogos().stream()
                        .map(jogoMapper::toResponse)
                        .toList();

        return new ListaPersonalizadaResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.isPublica(),
                entity.getUsuario().getId(), // Garante o ID do usuário dono da lista
                jogosDTO
        );
    }

    /**
     * Converte o DTO de criação para a Entidade.
     * Mapeia os dados simples (nome, publica).
     * Nota: Os relacionamentos (Usuario e Jogos) devem ser buscados e setados pelo Service.
     */
    public ListaPersonalizada toEntity(ListaPersonalizadaCriarRequestDTO dto) {
        return objectMapperUtil.map(dto, ListaPersonalizada.class);
    }
}