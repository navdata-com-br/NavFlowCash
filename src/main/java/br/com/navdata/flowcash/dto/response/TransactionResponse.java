package br.com.navdata.flowcash.dto.response;

import br.com.navdata.flowcash.entity.TransactionEntity;
import br.com.navdata.flowcash.entity.enums.PaymentMethod;
import br.com.navdata.flowcash.entity.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
    String id,
    String description,
    BigDecimal amount,
    LocalDateTime date,
    TransactionType type,
    PaymentMethod paymentMethod,
    Boolean isPaid,
    LocalDateTime paidAt,
    String categoryId,
    String accountId,
    Boolean isTransfer,
    String destinationAccountId,
    String transferId,
    Boolean isFixed,
    String installmentGroup,
    Integer installmentNumber,
    Integer totalInstallments,
    LocalDateTime createdAt
) {
    public static TransactionResponse fromEntity(TransactionEntity entity) {
        return new TransactionResponse(
            entity.getId(),
            entity.getDescription(),
            entity.getAmount(),
            entity.getDate(),
            entity.getType(),
            entity.getPaymentMethod(),
            entity.getIsPaid(),
            entity.getPaidAt(),
            entity.getCategoryId(),
            entity.getAccountId(),
            entity.getIsTransfer(),
            entity.getDestinationAccountId(),
            entity.getTransferId(),
            entity.getIsFixed(),
            entity.getInstallmentGroup(),
            entity.getInstallmentNumber(),
            entity.getTotalInstallments(),
            entity.getCreatedAt()
        );
    }
}
