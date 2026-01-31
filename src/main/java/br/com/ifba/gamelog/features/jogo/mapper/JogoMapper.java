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
         * Mapeamento manual para suportar Java Records (imutáveis).
         */
        public JogoResponseDTO toResponse(Jogo entity) {
            if (entity == null) return null;

            return new JogoResponseDTO(
                    entity.getId(),
                    entity.getIdExterno(),
                    entity.getTitulo(),
                    entity.getCapaUrl(),
                    entity.getDescricao(),
                    entity.getAnoLancamento(),
                    entity.getPlataformas(),
                    entity.getGenero()
            );
        }

        /**
         * Converte o DTO de criação para a entidade Jogo.
         * Aqui usamos o utilitário, pois a entidade Jogo é um POJO padrão.
         */
        public Jogo toEntity(JogoCriarRequestDTO dto) {
            return objectMapperUtil.map(dto, Jogo.class);
        }
    }