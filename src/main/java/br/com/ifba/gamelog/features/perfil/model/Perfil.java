package br.com.ifba.gamelog.features.perfil.model;

import br.com.ifba.gamelog.features.usuario.model.Usuario;
import br.com.ifba.gamelog.infrastructure.model.SimplePersistenceEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "perfis")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Perfil extends SimplePersistenceEntity { // ID é Long

    @Column(name = "avatar_imagem")
    private String avatarImagem;

    @Column(name = "biografia", length = 500)
    private String biografia;

    @Column(name = "nome_exibicao")
    private String nomeExibicao;

    // Relacionamento OneToOne: Um Perfil pertence a um Usuario.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;
}