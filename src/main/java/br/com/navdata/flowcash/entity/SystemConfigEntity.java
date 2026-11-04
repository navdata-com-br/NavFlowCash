package br.com.navdata.flowcash.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_system_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemConfigEntity {

    @Id
    private String id; // "payment" ou "general"

    @Column(name = "pix_key")
    private String pixKey;

    @Column(name = "pix_name")
    private String pixName;

    @Column(name = "pix_city")
    private String pixCity;

    @Column(name = "mp_access_token")
    private String mpAccessToken;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
