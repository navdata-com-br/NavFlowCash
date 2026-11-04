package br.com.navdata.flowcash.entity;

import br.com.navdata.flowcash.entity.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionType type;

    @Column(nullable = false, length = 50)
    private String icon;

    @Column(nullable = false, length = 30)
    private String color;
}
