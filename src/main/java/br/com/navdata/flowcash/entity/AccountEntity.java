package br.com.navdata.flowcash.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_accounts")
public class AccountEntity {

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
    private String name;

    @Column(nullable = false, length = 30)
    private String type;

    private String bank;

    @Column(name = "initial_balance", nullable = false, precision = 12, scale = 2)
    private BigDecimal initialBalance = BigDecimal.ZERO;

    @Column(length = 20)
    private String color;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public AccountEntity() {}

    private AccountEntity(Builder builder) {
        id = builder.id;
        userId = builder.userId;
        userName = builder.userName;
        userEmail = builder.userEmail;
        name = builder.name;
        type = builder.type;
        bank = builder.bank;
        initialBalance = builder.initialBalance;
        color = builder.color;
        createdAt = builder.createdAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private String userId;
        private String userName;
        private String userEmail;
        private String name;
        private String type;
        private String bank;
        private BigDecimal initialBalance = BigDecimal.ZERO;
        private String color;
        private LocalDateTime createdAt;

        public Builder id(String value) { id = value; return this; }
        public Builder userId(String value) { userId = value; return this; }
        public Builder userName(String value) { userName = value; return this; }
        public Builder userEmail(String value) { userEmail = value; return this; }
        public Builder name(String value) { name = value; return this; }
        public Builder type(String value) { type = value; return this; }
        public Builder bank(String value) { bank = value; return this; }
        public Builder initialBalance(BigDecimal value) { initialBalance = value; return this; }
        public Builder color(String value) { color = value; return this; }
        public Builder createdAt(LocalDateTime value) { createdAt = value; return this; }
        public AccountEntity build() { return new AccountEntity(this); }
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getBank() { return bank; }
    public void setBank(String bank) { this.bank = bank; }

    public BigDecimal getInitialBalance() { return initialBalance; }
    public void setInitialBalance(BigDecimal initialBalance) { this.initialBalance = initialBalance; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
