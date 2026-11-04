package br.com.navdata.flowcash.entity;

import br.com.navdata.flowcash.entity.enums.PaymentMethod;
import br.com.navdata.flowcash.entity.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_email")
    private String userEmail;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 30)
    private PaymentMethod paymentMethod;

    @Column(name = "is_paid", nullable = false)
    @Builder.Default
    private Boolean isPaid = false;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "is_transfer")
    @Builder.Default
    private Boolean isTransfer = false;

    @Column(name = "destination_account_id")
    private String destinationAccountId;

    @Column(name = "transfer_id")
    private String transferId;

    @Column(name = "is_fixed")
    @Builder.Default
    private Boolean isFixed = false;

    @Column(name = "installment_group")
    private String installmentGroup;

    @Column(name = "installment_number")
    private Integer installmentNumber;

    @Column(name = "total_installments")
    private Integer totalInstallments;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.isPaid == null) this.isPaid = false;
        if (this.isFixed == null) this.isFixed = false;
        if (this.isTransfer == null) this.isTransfer = false;
    }
}
