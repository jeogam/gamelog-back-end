package br.com.ifba.gamelog.features.avaliacao.model;

import br.com.ifba.gamelog.features.jogo.model.Jogo;
import br.com.ifba.gamelog.features.usuario.model.Usuario;
import br.com.ifba.gamelog.infrastructure.model.PersistenceEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "avaliacoes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Avaliacao extends PersistenceEntity {

    @Column(nullable = false)
    private Integer nota; // 1 a 5

    @Column(columnDefinition = "TEXT")
    private String comentario;

    // 'criadoEm' removido, usará o 'createdAt' da PersistenceEntity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jogo_id", nullable = false)
    private Jogo jogo;
}