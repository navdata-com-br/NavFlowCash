package br.com.navdata.flowcash.dto.request;

import br.com.navdata.flowcash.entity.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryRequest(
    @NotBlank(message = "O nome é obrigatório")
    String name,
    @NotNull(message = "O tipo é obrigatório")
    TransactionType type,
    @NotBlank(message = "O ícone é obrigatório")
    String icon,
    @NotBlank(message = "A cor é obrigatória")
    String color
) {}
