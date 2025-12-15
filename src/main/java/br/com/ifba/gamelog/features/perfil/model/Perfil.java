package br.com.ifba.gamelog.features.perfil.model;

import br.com.ifba.gamelog.infrastructure.model.SimplePersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "perfis")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Perfil extends SimplePersistenceEntity {

    @Column(name = "avatar_imagem", nullable = true)
    private String avatarImagem;

    @Column(name = "biografia", nullable = true)
    private String biografia;

    @Column(name = "nome_exibicao", nullable = true)
    private String nomeExibicao;

}
