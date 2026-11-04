package br.com.navdata.flowcash.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PartnerRequest(
    @NotBlank(message = "O e-mail do parceiro é obrigatório")
    @Email(message = "E-mail inválido")
    String email,

    String name
) {}
