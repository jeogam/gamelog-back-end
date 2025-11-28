package br.com.ifba.gamelog.features.jogo.model;

import br.com.ifba.gamelog.features.avaliacao.model.Avaliacao;
import br.com.ifba.gamelog.features.biblioteca.model.Biblioteca;
import br.com.ifba.gamelog.features.lista.model.ListaPersonalizada;
import br.com.ifba.gamelog.infrastructure.PersistenceEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "jogos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Jogo extends PersistenceEntity {

    @Column(name = "id_externo", unique = true, nullable = false)
    private Long idExterno;

    @Column(nullable = false)
    private String titulo;

    @Column(name = "capa_url")
    private String capaUrl;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "ano_lancamento")
    private Integer anoLancamento;

    private String plataformas;

    private String genero;

    // 1. Um jogo recebe várias avaliações
    @OneToMany(mappedBy = "jogo", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Avaliacao> avaliacoes;

    // 2. Um jogo está na biblioteca de várias pessoas
    @OneToMany(mappedBy = "jogo", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Biblioteca> emBibliotecas;

    // 3. Um jogo pode estar em várias listas personalizadas
    @ManyToMany(mappedBy = "jogos")
    @JsonIgnore
    private List<ListaPersonalizada> emListas;

}