package br.com.ifba.gamelog.infrastructure.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Entidade base para persistência (usa Long como ID).
 * <p>Pode ser estendida por outras entidades JPA do GameLog.</p>
 */
@MappedSuperclass
public class SimplePersistenceEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include @ToString.Include
    @Getter
    private Long id;
}