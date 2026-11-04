package br.com.navdata.flowcash.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String role = "user";

    @Column(name = "access_until")
    private LocalDateTime accessUntil;

    @Column(name = "categories_seeded")
    @Builder.Default
    private Boolean categoriesSeeded = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_access")
    private LocalDateTime lastAccess;

    @Column(name = "partner_email")
    private String partnerEmail;

    @Column(name = "partner_name")
    private String partnerName;

    @Column(name = "partner_uid")
    private String partnerUid;

    @Column(name = "invited_by")
    private String invitedBy;

    @Column(name = "primary_owner_email")
    private String primaryOwnerEmail;

    @Column(name = "primary_owner_name")
    private String primaryOwnerName;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.lastAccess = LocalDateTime.now();
        if (this.accessUntil == null) {
            this.accessUntil = LocalDateTime.now().plusDays(7);
        }
    }

    public boolean isAccessExpired() {
        return accessUntil != null && accessUntil.isBefore(LocalDateTime.now());
    }
}
