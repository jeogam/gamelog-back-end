package br.com.ifba.gamelog.features.biblioteca.model;

import br.com.ifba.gamelog.features.jogo.model.Jogo;
import br.com.ifba.gamelog.features.usuario.model.Usuario;
import br.com.ifba.gamelog.infrastructure.model.PersistenceEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "biblioteca", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"usuario_id", "jogo_id"})
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Biblioteca extends PersistenceEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusJogo status; // Enum: QUERO_JOGAR, JOGANDO, ZERADO, ABANDONADO

    @Column(name = "favorito", nullable = false)
    private boolean favorito = false;

    // Relacionamentos Lazy para performance
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jogo_id", nullable = false)
    private Jogo jogo;
}