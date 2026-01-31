package br.com.ifba.gamelog.features.perfil.mapper;

import br.com.ifba.gamelog.features.perfil.dto.request.PerfilCriarRequestDTO;
import br.com.ifba.gamelog.features.perfil.dto.response.PerfilResponseDTO;
import br.com.ifba.gamelog.features.perfil.model.Perfil;
import br.com.ifba.gamelog.infrastructure.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Componente responsável pelo mapeamento entre Entidades (Perfil) e DTOs.
 * <p>
 * Realiza a conversão de dados para entrada (criação/atualização) e saída (resposta),
 * garantindo que informações sensíveis ou estruturais (como o ID do usuário e Papel)
 * sejam tratadas corretamente.
 * </p>
 *
 * @author Jeovani Nunes
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class PerfilMapper {

    private final ObjectMapperUtil objectMapperUtil;

    /**
     * Converte a entidade {@link Perfil} para o DTO de resposta {@link PerfilResponseDTO}.
     * <p>
     * Este método realiza um mapeamento manual parcial para extrair informações
     * que estão na entidade {@code Usuario} (como o ID e o Papel) e achatá-las
     * no objeto de resposta do perfil.
     * </p>
     *
     * @param entity A entidade Perfil recuperada do banco.
     * @return O DTO contendo os dados do perfil e o papel do usuário, ou {@code null} se a entidade for nula.
     */
    public PerfilResponseDTO toResponse(Perfil entity) {
        if (entity == null) return null;

        // ✅ Lógica de Extração: O papel pertence ao Usuário, mas o Front precisa dele no Perfil.
        // Define "USUARIO" como padrão caso haja alguma inconsistência.
        String papel = "USUARIO";
        if (entity.getUsuario() != null && entity.getUsuario().getPapel() != null) {
            papel = entity.getUsuario().getPapel().name();
        }

        return new PerfilResponseDTO(
                entity.getId(),
                entity.getNomeExibicao(),
                entity.getBiografia(),
                entity.getAvatarImagem(),
                entity.getUsuario() != null ? entity.getUsuario().getId() : null, // Evita NullPointerException
                papel // ✅ Campo preenchido com o dado vindo de Usuario
        );
    }

    /**
     * Converte o DTO de criação {@link PerfilCriarRequestDTO} para a entidade {@link Perfil}.
     * <p>
     * Utiliza o {@link ObjectMapperUtil} para realizar o mapeamento automático das propriedades
     * com nomes idênticos.
     * </p>
     *
     * @param dto O DTO com os dados de criação.
     * @return A entidade Perfil pronta para ser persistida (sem o ID gerado ainda).
     */
    public Perfil toEntity(PerfilCriarRequestDTO dto) {
        return objectMapperUtil.map(dto, Perfil.class);
    }
}