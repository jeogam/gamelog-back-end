package br.com.ifba.gamelog.features.jogo.model;

import br.com.ifba.gamelog.features.avaliacao.model.Avaliacao;
import br.com.ifba.gamelog.features.biblioteca.model.Biblioteca;
import br.com.ifba.gamelog.features.lista.model.ListaPersonalizada;
import br.com.ifba.gamelog.infrastructure.model.PersistenceEntity;
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
@EqualsAndHashCode(callSuper = true)
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

    // Se no futuro quiser transformar em tabelas, mude aqui. Por enquanto String está ok.
    private String plataformas;
    private String genero;

    @OneToMany(mappedBy = "jogo", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Avaliacao> avaliacoes;

    @OneToMany(mappedBy = "jogo", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Biblioteca> emBibliotecas;

    @ManyToMany(mappedBy = "jogos")
    @JsonIgnore
    private List<ListaPersonalizada> emListas;
}