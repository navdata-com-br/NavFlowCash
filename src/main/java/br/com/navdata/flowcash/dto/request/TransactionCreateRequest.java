package br.com.navdata.flowcash.dto.request;

import br.com.navdata.flowcash.entity.enums.PaymentMethod;
import br.com.navdata.flowcash.entity.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionCreateRequest(
    @NotBlank(message = "A descrição é obrigatória")
    String description,
    @NotNull(message = "O valor é obrigatório")
    @Positive(message = "O valor deve ser positivo")
    BigDecimal amount,
    @NotNull(message = "A data é obrigatória")
    LocalDateTime date,
    @NotNull(message = "O tipo é obrigatório")
    TransactionType type,
    @NotNull(message = "A forma de pagamento é obrigatória")
    PaymentMethod paymentMethod,
    @NotBlank(message = "A categoria é obrigatória")
    String categoryId,
    @NotBlank(message = "A conta é obrigatória")
    String accountId,
    Boolean isPaid,
    Boolean isFixed,
    Integer installments
) {}
