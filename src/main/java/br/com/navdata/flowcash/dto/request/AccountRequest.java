package br.com.navdata.flowcash.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record AccountRequest(
    @NotBlank(message = "O nome da conta é obrigatório")
    String name,
    @NotBlank(message = "O tipo da conta é obrigatório")
    String type,
    String bank,
    @NotNull(message = "O saldo inicial é obrigatório")
    BigDecimal initialBalance,
    String color
) {}
