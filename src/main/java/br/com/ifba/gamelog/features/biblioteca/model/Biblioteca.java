package br.com.ifba.gamelog.features.biblioteca.model;

import br.com.ifba.gamelog.features.jogo.model.Jogo;
import br.com.ifba.gamelog.features.usuario.model.Usuario;
import br.com.ifba.gamelog.infrastructure.PersistenceEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "biblioteca", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"usuario_id", "jogo_id"})
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Biblioteca extends PersistenceEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusJogo status;

    @Column(name = "favorito", nullable = false)
    private boolean favorito = false;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jogo_id", nullable = false)
    private Jogo jogo;
}