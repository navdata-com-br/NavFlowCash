package br.com.navdata.flowcash.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_closed_months")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClosedMonthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "closed_month", nullable = false)
    private LocalDateTime month;

    @Column(name = "closed_at", nullable = false)
    private LocalDateTime closedAt;

    @PrePersist
    protected void onCreate() {
        this.closedAt = LocalDateTime.now();
    }
}
