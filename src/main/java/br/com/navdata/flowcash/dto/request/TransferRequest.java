package br.com.navdata.flowcash.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferRequest(
    @NotNull(message = "O valor é obrigatório")
    @Positive(message = "O valor deve ser positivo")
    BigDecimal amount,
    @NotNull(message = "A data é obrigatória")
    LocalDateTime date,
    @NotBlank(message = "Conta de origem é obrigatória")
    String sourceAccountId,
    @NotBlank(message = "Conta de destino é obrigatória")
    String destinationAccountId
) {}
