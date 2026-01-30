package br.com.ifba.gamelog.features.biblioteca.mapper;

import br.com.ifba.gamelog.features.biblioteca.dto.request.BibliotecaCriarRequestDTO;
import br.com.ifba.gamelog.features.biblioteca.dto.response.BibliotecaResponseDTO;
import br.com.ifba.gamelog.features.biblioteca.model.Biblioteca;
import br.com.ifba.gamelog.infrastructure.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BibliotecaMapper {

    private final ObjectMapperUtil objectMapperUtil;

    /**
     * Converte a entidade Biblioteca para o DTO de resposta.
     * Segue o padrão de instanciar o Record manualmente para evitar erros de reflexão
     * e para extrair dados aninhados (Titulo e Capa do Jogo) de forma segura.
     */
    public BibliotecaResponseDTO toResponse(Biblioteca entity) {
        if (entity == null) return null;

        return new BibliotecaResponseDTO(
                entity.getId(),
                entity.getStatus(),
                entity.isFavorito(),
                entity.getUsuario().getId(),
                entity.getJogo().getId(),
                entity.getJogo().getTitulo(), // Flattening: Trazendo o título para a raiz do DTO
                entity.getJogo().getCapaUrl() // Flattening: Trazendo a capa para a raiz do DTO
        );
    }

    /**
     * Converte o DTO de criação para Entidade.
     * Segue o padrão de usar o ObjectMapperUtil, já que Biblioteca é um POJO padrão.
     */
    public Biblioteca toEntity(BibliotecaCriarRequestDTO dto) {
        return objectMapperUtil.map(dto, Biblioteca.class);
    }
}