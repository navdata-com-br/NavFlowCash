package br.com.navdata.flowcash.entity;

import br.com.navdata.flowcash.entity.enums.PaymentMethod;
import br.com.navdata.flowcash.entity.enums.TransactionType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_transactions")
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
    private Boolean isPaid = false;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "is_transfer")
    private Boolean isTransfer = false;

    @Column(name = "destination_account_id")
    private String destinationAccountId;

    @Column(name = "transfer_id")
    private String transferId;

    @Column(name = "is_fixed")
    private Boolean isFixed = false;

    @Column(name = "installment_group")
    private String installmentGroup;

    @Column(name = "installment_number")
    private Integer installmentNumber;

    @Column(name = "total_installments")
    private Integer totalInstallments;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public TransactionEntity() {}

    private TransactionEntity(Builder builder) {
        id = builder.id;
        userId = builder.userId;
        userName = builder.userName;
        userEmail = builder.userEmail;
        description = builder.description;
        amount = builder.amount;
        date = builder.date;
        type = builder.type;
        paymentMethod = builder.paymentMethod;
        isPaid = builder.isPaid;
        paidAt = builder.paidAt;
        categoryId = builder.categoryId;
        accountId = builder.accountId;
        isTransfer = builder.isTransfer;
        destinationAccountId = builder.destinationAccountId;
        transferId = builder.transferId;
        isFixed = builder.isFixed;
        installmentGroup = builder.installmentGroup;
        installmentNumber = builder.installmentNumber;
        totalInstallments = builder.totalInstallments;
        createdAt = builder.createdAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private String userId;
        private String userName;
        private String userEmail;
        private String description;
        private BigDecimal amount;
        private LocalDateTime date;
        private TransactionType type;
        private PaymentMethod paymentMethod;
        private Boolean isPaid = false;
        private LocalDateTime paidAt;
        private String categoryId;
        private String accountId;
        private Boolean isTransfer = false;
        private String destinationAccountId;
        private String transferId;
        private Boolean isFixed = false;
        private String installmentGroup;
        private Integer installmentNumber;
        private Integer totalInstallments;
        private LocalDateTime createdAt;

        public Builder id(String value) { id = value; return this; }
        public Builder userId(String value) { userId = value; return this; }
        public Builder userName(String value) { userName = value; return this; }
        public Builder userEmail(String value) { userEmail = value; return this; }
        public Builder description(String value) { description = value; return this; }
        public Builder amount(BigDecimal value) { amount = value; return this; }
        public Builder date(LocalDateTime value) { date = value; return this; }
        public Builder type(TransactionType value) { type = value; return this; }
        public Builder paymentMethod(PaymentMethod value) { paymentMethod = value; return this; }
        public Builder isPaid(Boolean value) { isPaid = value; return this; }
        public Builder paidAt(LocalDateTime value) { paidAt = value; return this; }
        public Builder categoryId(String value) { categoryId = value; return this; }
        public Builder accountId(String value) { accountId = value; return this; }
        public Builder isTransfer(Boolean value) { isTransfer = value; return this; }
        public Builder destinationAccountId(String value) { destinationAccountId = value; return this; }
        public Builder transferId(String value) { transferId = value; return this; }
        public Builder isFixed(Boolean value) { isFixed = value; return this; }
        public Builder installmentGroup(String value) { installmentGroup = value; return this; }
        public Builder installmentNumber(Integer value) { installmentNumber = value; return this; }
        public Builder totalInstallments(Integer value) { totalInstallments = value; return this; }
        public Builder createdAt(LocalDateTime value) { createdAt = value; return this; }
        public TransactionEntity build() { return new TransactionEntity(this); }
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.isPaid == null) this.isPaid = false;
        if (this.isFixed == null) this.isFixed = false;
        if (this.isTransfer == null) this.isTransfer = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public Boolean getIsPaid() { return isPaid; }
    public void setIsPaid(Boolean isPaid) { this.isPaid = isPaid; }

    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public Boolean getIsTransfer() { return isTransfer; }
    public void setIsTransfer(Boolean isTransfer) { this.isTransfer = isTransfer; }

    public String getDestinationAccountId() { return destinationAccountId; }
    public void setDestinationAccountId(String destinationAccountId) { this.destinationAccountId = destinationAccountId; }

    public String getTransferId() { return transferId; }
    public void setTransferId(String transferId) { this.transferId = transferId; }

    public Boolean getIsFixed() { return isFixed; }
    public void setIsFixed(Boolean isFixed) { this.isFixed = isFixed; }

    public String getInstallmentGroup() { return installmentGroup; }
    public void setInstallmentGroup(String installmentGroup) { this.installmentGroup = installmentGroup; }

    public Integer getInstallmentNumber() { return installmentNumber; }
    public void setInstallmentNumber(Integer installmentNumber) { this.installmentNumber = installmentNumber; }

    public Integer getTotalInstallments() { return totalInstallments; }
    public void setTotalInstallments(Integer totalInstallments) { this.totalInstallments = totalInstallments; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
