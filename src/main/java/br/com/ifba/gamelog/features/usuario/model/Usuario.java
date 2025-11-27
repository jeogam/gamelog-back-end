package br.com.ifba.gamelog.features.usuario.model;

import br.com.ifba.gamelog.infrastructure.PersistenceEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Usuario extends PersistenceEntity {

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

}
