package br.com.ifba.gamelog.features.usuario.model;

import br.com.ifba.gamelog.features.avaliacao.model.Avaliacao;
import br.com.ifba.gamelog.features.biblioteca.model.Biblioteca;
import br.com.ifba.gamelog.features.lista.model.ListaPersonalizada;
import br.com.ifba.gamelog.infrastructure.model.PersistenceEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Usuario extends PersistenceEntity {

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "senha", nullable = false)
    private String senha;

    // 'criadoEm' foi removido pois já existe na PersistenceEntity como 'createdAt'

    // --- Relacionamentos ---

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Biblioteca> biblioteca;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Avaliacao> avaliacoes;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ListaPersonalizada> listasCriadas;
}