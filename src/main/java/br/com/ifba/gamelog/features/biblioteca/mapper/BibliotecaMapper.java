package br.com.ifba.gamelog.features.biblioteca.mapper;

import br.com.ifba.gamelog.features.biblioteca.dto.response.BibliotecaResponseDTO;
import br.com.ifba.gamelog.features.biblioteca.model.Biblioteca;
import org.springframework.stereotype.Component;

@Component
public class BibliotecaMapper {

    /**
     * Converte a entidade Biblioteca para o DTO de resposta.
     * Mapeia manualmente os dados aninhados do Jogo (Titulo e Capa) para facilitar o frontend.
     */
    public BibliotecaResponseDTO toResponse(Biblioteca entity) {
        if (entity == null) return null;

        return new BibliotecaResponseDTO(
                entity.getId(),
                entity.getStatus(),
                entity.isFavorito(),
                entity.getUsuario().getId(),
                entity.getJogo().getId(),
                entity.getJogo().getTitulo(),
                entity.getJogo().getCapaUrl()
        );
    }
}