package br.com.navdata.flowcash.dto.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentTransactionRequest(
        @NotNull Integer systemUnitId,
        @NotNull Integer planId,
        BigDecimal amount,
        String externalReference
) {
}
