package br.com.ifba.gamelog.infrastructure.model;

import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist; // ⬅️ Novo Import
import jakarta.persistence.PreUpdate;   // ⬅️ Novo Import
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;
import java.io.Serializable;
import java.time.Instant; // ⬅️ Novo Import
import java.util.UUID;

@Data
@MappedSuperclass
public class PersistenceEntity implements Serializable {

    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    private UUID id;

    // Campos de Auditoria
    private Instant createdAt;
    private Instant updatedAt;

    // Métodos para preencher automaticamente as datas
    @PrePersist
    protected void onCreate() {
        // Define o createdAt e o updatedAt no momento da criação
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        // Atualiza o updatedAt em cada operação de atualização
        this.updatedAt = Instant.now();
    }
}