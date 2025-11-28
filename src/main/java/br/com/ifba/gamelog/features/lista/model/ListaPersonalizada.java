package br.com.ifba.gamelog.features.lista.model;

import br.com.ifba.gamelog.features.jogo.model.Jogo;
import br.com.ifba.gamelog.features.usuario.model.Usuario;
import br.com.ifba.gamelog.infrastructure.PersistenceEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "listas_personalizadas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListaPersonalizada extends PersistenceEntity {

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private boolean publica = true;

    // Relacionamento N para 1 com Usuario (Quem criou a lista)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Relacionamento N para N com Jogo (A lista contém vários jogos)
    @ManyToMany
    @JoinTable(
            name = "itens_lista",
            joinColumns = @JoinColumn(name = "lista_id"),
            inverseJoinColumns = @JoinColumn(name = "jogo_id")
    )
    private List<Jogo> jogos;
}