package br.com.ifba.gamelog.features.biblioteca.model;

import br.com.ifba.gamelog.features.jogo.model.Jogo;
import br.com.ifba.gamelog.features.usuario.model.Usuario;
import br.com.ifba.gamelog.infrastructure.model.PersistenceEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "biblioteca", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"usuario_id", "jogo_id"}) // Garante que o user não tenha o mesmo jogo 2x na lib
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Biblioteca extends PersistenceEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusJogo status;

    @Column(name = "favorito", nullable = false)
    private boolean favorito = false;

    // 'atualizadoEm' removido, usará o 'updatedAt' da PersistenceEntity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jogo_id", nullable = false)
    private Jogo jogo;
}